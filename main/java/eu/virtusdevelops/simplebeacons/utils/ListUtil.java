package eu.virtusdevelops.simplebeacons.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListUtil {
    public static <T> List<List<T>> getBatches(List<T> collection, int batchSize){
        int i = 0;
        List<List<T>> batches = new ArrayList<>();
        while(i<collection.size()){
            int nextInc = Math.min(collection.size()-i,batchSize);
            List<T> batch = collection.subList(i,i+nextInc);
            batches.add(batch);
            i = i + nextInc;
        }

        return batches;
    }

    public static <T> List<String> convertToList(Collection<T> collection, ConversionStrategy<T> strategy) {

        List<String> converted = new ArrayList<>(collection.size());

        for (T obj : collection) {
            String convert = strategy.convertToString(obj);
            if (convert != null) converted.add(convert);
        }

        return converted;
    }

    public static interface ConversionStrategy<T> {

        public String convertToString(T input);

    }


}
