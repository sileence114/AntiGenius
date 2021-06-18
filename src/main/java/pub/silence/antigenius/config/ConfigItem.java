package pub.silence.antigenius.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigItem<T> {
    // About Suggest
    private final List<T> suggestValues = new ArrayList<T>();
    private boolean forceSuggest = false;
    // About Interval
    private double intervalStart = 0;
    private double intervalEnd = 0;
    private boolean forceRange = false;
    // About Value
    private T value;
    private T defaultValue;
    // Sort Id
    private int id;
    public ConfigItem(T defaultValue, int sort){
        this.value = this.defaultValue = defaultValue;
        this.id = sort;
    }
    
    // About Suggest
    public ConfigItem<T> setSuggest(T[] suggests){
        this.suggestValues.addAll(Arrays.asList(suggests));
        return this;
    }
    public ConfigItem<T> setSuggest(List<T> suggests){
        this.suggestValues.addAll(suggests);
        return this;
    }
    public List<T> getSuggest(){
        return this.suggestValues;
    }
    public ConfigItem<T> forceSuggest(boolean force){
        this.forceSuggest = force;
        return this;
    }
    public ConfigItem<T> forceSuggest(){
        this.forceSuggest = true;
        return this;
    }
    public boolean isForceSuggest(){
        return this.forceSuggest;
    }
    
    // About Interval
    public ConfigItem<T> setRange(Number start, Number end){
        if(defaultValue.getClass().getSuperclass().equals(Number.class)){
            this.intervalStart = start.doubleValue();
            this.intervalEnd = end.doubleValue();
            this.forceRange = true;
        }
        return this;
    }
    public ConfigItem<T> forceRange(boolean force){
        this.forceRange = force;
        return this;
    }
    public double[] getRange(){
        return new double[] {this.intervalStart, this.intervalEnd};
    }
    public boolean isForceRange(){
        return this.forceRange;
    }
    
    // About Default
    public ConfigItem<T> setDefaultValue(T defaultVal){
        this.defaultValue = defaultVal;
        return this;
    }
    public T getDefaultValue(){
        return this.defaultValue;
    }
    
    // About Value
    public ConfigItem<T> set(Object newValue) {
        T val = (T)newValue;
        if(this.forceSuggest){
            if(this.suggestValues.contains(val)){
                this.value = val;
            }
        }
        else if(this.forceRange){
            double doubleValue = ((Number)val).doubleValue();
            if(doubleValue >= this.intervalStart && doubleValue < this.intervalEnd){
                this.value = val;
            }
        }
        else{
            this.value = val;
        }
        return this;
    }
    public ConfigItem<T> reset(){
        this.value = this.defaultValue;
        return this;
    }
    public T get(){
        return this.value;
    }
    public Class<?> type(){
        return this.defaultValue.getClass();
    }
    public int getSortId(){
        return this.id;
    }
}
