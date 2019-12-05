package DigitRecogniser;

import static java.lang.String.format;

import java.io.ByteArrayOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * A reader for the MNIST database
 * Uses magic number to store each element 
 *
 * @author codingmroberts
 *
 */

public class readerMNIST {
    
        // Magic numbers for reading the MNIST dataset
	public static final int LABEL_FILE_MAGIC_NUMBER = 2049;
	public static final int IMAGE_FILE_MAGIC_NUMBER = 2051;

        /**
        * Gets a integer array of all the labels stored in the given file
        * Uses MNIST magic number
        */
	public static int[] getLabels(String infile) {
		ByteBuffer bb = loadFileToByteBuffer(infile);

		assertMagicNumber(LABEL_FILE_MAGIC_NUMBER, bb.getInt());

		int numLabels = bb.getInt();
		int[] labels = new int[numLabels];

		for (int i = 0; i < numLabels; ++i)
			labels[i] = bb.get() & 0xFF; // To unsigned

		return labels;
	}

        /**
        * Gets a 2D array of integers that represent the images stored in MNIST database
        */
	public static List<int[][]> getImages(String infile) {
		ByteBuffer bb = loadFileToByteBuffer(infile);

		assertMagicNumber(IMAGE_FILE_MAGIC_NUMBER, bb.getInt());

		int numImages = bb.getInt();
		int numRows = bb.getInt();
		int numColumns = bb.getInt();
		List<int[][]> images = new ArrayList<>();

		for (int i = 0; i < numImages; i++)
			images.add(readImage(numRows, numColumns, bb));

		return images;
	}
        /**
        * Gets the images from getImages and returns a single dimensional representation of the images
        */
        public static List<int[]> getImages2(String infile) {
            
            List<int[][]> images = getImages(infile);
            List<int[]> images1d = new ArrayList();
            
            for (int i = 0; i < images.size(); i++){
                ArrayList<Integer> numbers1Dim = new ArrayList();

                    for (int j = 0; j < images.get(i).length; j++)
                    {
                        for (int x = 0; x < images.get(i)[j].length; x++)
                            numbers1Dim.add(images.get(i)[j][x]);
                    }

                int[] number1d = new int[numbers1Dim.size()];
                    for (int j = 0; j < numbers1Dim.size(); j++)
                        number1d[j] = numbers1Dim.get(j);
                    
                images1d.add(number1d);
            }

            return images1d;
	}

        /**
        * Reads a single image
        */
	private static int[][] readImage(int numRows, int numCols, ByteBuffer bb) {
		int[][] image = new int[numRows][];
		for (int row = 0; row < numRows; row++)
			image[row] = readRow(numCols, bb);
		return image;
	}
        
        /**
        * Reads a row of a single image and returns an array of integers
        */
	private static int[] readRow(int numCols, ByteBuffer bb) {
		int[] row = new int[numCols];
		for (int col = 0; col < numCols; ++col)
			row[col] = bb.get() & 0xFF; // To unsigned
		return row;
	}
        /**
        * Asserts the magic number when reading from the MNIST database
        */
	public static void assertMagicNumber(int expectedMagicNumber, int magicNumber) {
		if (expectedMagicNumber != magicNumber) {
			switch (expectedMagicNumber) {
			case LABEL_FILE_MAGIC_NUMBER:
				throw new RuntimeException("This is not a label file.");
			case IMAGE_FILE_MAGIC_NUMBER:
				throw new RuntimeException("This is not an image file.");
			default:
				throw new RuntimeException(
						format("Expected magic number %d, found %d", expectedMagicNumber, magicNumber));
			}
		}
	}

        /**
        * Loads file into byte buffer
        */
	public static ByteBuffer loadFileToByteBuffer(String infile) {
		return ByteBuffer.wrap(loadFile(infile));
	}

        /**
        * loads file into a array of bytes
        */
	public static byte[] loadFile(String infile) {
		try {
			RandomAccessFile f = new RandomAccessFile(infile, "r");
			FileChannel chan = f.getChannel();
			long fileSize = chan.size();
			ByteBuffer bb = ByteBuffer.allocate((int) fileSize);
			chan.read(bb);
			bb.flip();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			for (int i = 0; i < fileSize; i++)
				baos.write(bb.get());
			chan.close();
			f.close();
			return baos.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String repeat(String s, int n) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++)
			sb.append(s);
		return sb.toString();
	}
}