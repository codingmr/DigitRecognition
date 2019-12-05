/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DigitRecogniser;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import static DigitRecogniser.Main.bubbleSort;

/**
 * A class object that holds datasets for the training data and test data
 * Uses Euclidean distance to perform a knn algorithm classification
 * @author codingmroberts
 */
public class Dataset {
    
    private final List<int[]> imagesListTrain = new ArrayList();
    private final List<String> labelsListTrain = new ArrayList();
    
    private final List<int[]> imagesListTest = new ArrayList();
    private final List<String> labelsListTest = new ArrayList();
    
    private final List<Double[][]> distances = new ArrayList();
    private final List<Integer> predictionList = new ArrayList();
    private int predictionValue;

    /**
     * Getter for train images
     * @return imagesListTrain
     */
    public List<int[]> getImagesList() {
        return imagesListTrain;
    }

    /**
     * Getter for train labels
     * @return labelsListTrain
     */
    public List<String> getLabelsList() {
        return labelsListTrain;
    }    

    /**
     * Getter for classification prediction
     * @param idx
     * @return predictionList
     */
    public int getPrediction(int idx) {
        return predictionList.get(idx);
    }
    
    /**
     * Gets the accuracies of the classification
     * @return double
     */
    public double getAccuracy(){
        // reverse
        // Calc the number of times label matches the prediction
        double count = 0;
        
        for (int i=0; i < predictionList.size(); i++){
            if (Objects.equals(predictionList.get(i), Integer.valueOf(labelsListTrain.get(i))))
                    count++;
        }

        return (count/predictionList.size())*100;
    }
    
    /**
     * Initialises the dataset with the train database
     * @param imagesListTrain
     * @param labelsListTrain
     */
    public Dataset(List<int[]> imagesListTrain, int[] labelsListTrain){
        this.imagesListTrain.addAll(imagesListTrain);
    
        for (int i = 0; i < labelsListTrain.length; i++){
            this.labelsListTrain.add(Integer.toString(labelsListTrain[i]));
        }
    }
    
    /**
     * Adds images to the test list
     * @param images
     */
    public void addImagesListTest(List<int[]> images){
        this.imagesListTest.addAll(images);
    }
    
    /**
     * Adds labels to the test list
     * @param labels
     */
    public void addLabelsListTest(int[] labels){
        for (int i = 0; i < labels.length; i++){
            this.labelsListTest.add(Integer.toString(labels[i]));
        }
    }
    
    /**
     * Adds the Euclidean distances between points
     * @param distances
     */
    public void addEuclideanDistance(Double[][] distances){
        this.distances.add(distances);
    }
    
    /**
     * Adds the prediction to the list
     * @param predict
     */
    public void addPrediction(int predict){
        this.predictionList.add(predict);
    }
    
    /**
     * Gets the prediction value for guessing the digit
     * @return predictionValue
     */
    public int getPredictionValue(){
        return this.predictionValue;
    }
    
    /**
     * Testing the training model against the test dataset
     */
    public void testModel(){
        List<Double> distances = new ArrayList();
        double[] dist = new double[this.predictionList.size()];
        int[] idx = new int[this.predictionList.size()];
                
        // limit the number of data to the size of the trained model
        int dataSize = this.predictionList.size();
        
        
        for (int i = 0; i < dataSize; i++){
            for (int j = 0; j < dataSize; j++){
                dist[j] = getEuclideanDistance(this.imagesListTest.get(i), this.imagesListTest.get(j));
            }
            
                // populate index
            for (int j = 0; j < dataSize; j++){
                idx[j] = j;
            }


            int n = dataSize;
            bubbleSort(dist, idx, n);

            int[] smallLabel = new int[9];

            for (int k = 1; k < 9+1; k++){
                smallLabel[k-1] = this.predictionList.get(idx[k]);
            }           
        }
    }
    
    /**
     * Performs classification on the training dataset and adds predictionList
     */
    public void trainModel(){
        List<Double> distances = new ArrayList();
        double[] dist = new double[this.imagesListTrain.size()];
        int[] idx = new int[this.imagesListTrain.size()];
                
        // limit the number of data to save time
        int dataSize = 60000;
        
        
        for (int i = 0; i < dataSize; i++){
            for (int j = 0; j < this.imagesListTrain.size(); j++){
                dist[j] = getEuclideanDistance(this.imagesListTrain.get(i),this.imagesListTrain.get(j));
            }
            
                // populate index
            for (int j = 0; j < this.imagesListTrain.size(); j++){
                idx[j] = j;
            }


            int n = this.imagesListTrain.size();
            bubbleSort(dist, idx, n);

            int[] smallLabel = new int[9];

            for (int k = 1; k < 9+1; k++){
                smallLabel[k-1] = Integer.valueOf(this.labelsListTrain.get(idx[k]));
            }
            this.addPrediction(mostFrequent(smallLabel, smallLabel.length));
        }
    }
    
    /**
     * Given an image it predicts its value by using the classification from predictionList
     * @param img
     */
    public void predict(int[][] img){
        //convert the image into a 1 dimensional int array
        int[] singleImg = convertSingleDim(img);
        
        double[] dist = new double[this.imagesListTrain.size()];
        int[] idx = new int[this.imagesListTrain.size()];
        
        int datasetSize = this.predictionList.size();
        
        for (int j = 0; j < datasetSize; j++){
                dist[j] = getEuclideanDistance(singleImg, this.imagesListTrain.get(j));
            }
            
            // populate index
            for (int j = 0; j < datasetSize; j++){
                idx[j] = j;
            }


            int n = datasetSize;
            bubbleSort(dist, idx, n);

            int[] smallLabel = new int[9];

            for (int k = 1; k < 9+1; k++){
                smallLabel[k-1] = this.predictionList.get(idx[k]);
            }
            this.predictionValue = mostFrequent(smallLabel, smallLabel.length);
    }
    
    /**
     * Converts a 2 dimensional array of ints into a single dimensional
     * @param arr
     * @return number1d
     */
    public int[] convertSingleDim(int[][] arr){

        System.out.println("Converting");
        ArrayList<Integer> numbers1Dim = new ArrayList<Integer>();

                    for (int j = 0; j < arr.length; j++)
                    {
                        for (int x = 0; x < arr[j].length; x++)
                            numbers1Dim.add(arr[j][x]);
                    }

                int[] number1d = new int[numbers1Dim.size()];
                    for (int j = 0; j < numbers1Dim.size(); j++)
                        number1d[j] = numbers1Dim.get(j);

                    System.out.println("Convert done");
            return number1d;
    }
    
    /**
     * gets the Euclidean distance between two points
     * @param features1
     * @param features2
     * @return double
     */
    public double getEuclideanDistance( int[] features1,  int[] features2) {
            double sum = 0;
            for (int i = 0; i < features1.length; i++) //applied Euclidean distance formula
                sum += Math.pow(features1[i] - features2[i], 2);
            return Math.sqrt(sum);
        }
    
    /**
     * Finds the most common elements within the array
    */
    static int mostFrequent(int arr[], int n) 
    { 
          
        // Sort the array 
        Arrays.sort(arr); 
          
        // find the max frequency using linear 
        // traversal 
        int max_count = 1, res = arr[0]; 
        int curr_count = 1; 
          
        for (int i = 1; i < n; i++) 
        { 
            if (arr[i] == arr[i - 1]) 
                curr_count++; 
            else 
            { 
                if (curr_count > max_count) 
                { 
                    max_count = curr_count; 
                    res = arr[i - 1]; 
                } 
                curr_count = 1; 
            } 
        } 
      
        // If last element is most frequent 
        if (curr_count > max_count) 
        { 
            max_count = curr_count; 
            res = arr[n - 1]; 
        } 
      
        return res;
    }
    
}
