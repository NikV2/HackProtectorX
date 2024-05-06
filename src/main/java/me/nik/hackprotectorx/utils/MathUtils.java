package me.nik.hackprotectorx.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class MathUtils {

    private MathUtils(){}

    /**
     * @param data - The set of numbers / data you want to find the skewness from
     * @return - The skewness running the standard skewness formula.
     * @See - https://en.wikipedia.org/wiki/Skewness
     */
    public static double getSkewness(final Collection<? extends Number> data) {

        double sum = 0;
        int count = 0;

        final List<Double> numbers = new ArrayList<>();

        // Get the sum of all the data and the amount via looping
        for (final Number number : data) {

            sum += number.doubleValue();

            count++;

            numbers.add(number.doubleValue());
        }

        // Sort the numbers to run the calculations in the next part
        Collections.sort(numbers);

        // Run the formula to get skewness
        final double mean = sum / count;

        final double median = (count % 2 != 0) ? numbers.get(count / 2) : (numbers.get((count - 1) / 2) + numbers.get(count / 2)) / 2;

        final double variance = getVariance(data);

        return 3 * (mean - median) / variance;
    }

    public static double getVariance(final Collection<? extends Number> data) {
        if (data.isEmpty()) return 0D;

        int count = 0;

        double sum = 0.0;
        double variance = 0.0;

        double average;

        for (final Number number : data) {
            sum += number.doubleValue();
            ++count;
        }

        average = sum / count;

        for (final Number number : data) {
            variance += Math.pow(number.doubleValue() - average, 2.0);
        }

        return variance;
    }

    public static double getDeviation(final Collection<? extends Number> nums) {
        if (nums.isEmpty()) return 0D;

        return Math.sqrt((getVariance(nums) / (nums.size() - 1)));
    }

    public static double getAverage(final Collection<? extends Number> nums) {
        if (nums.isEmpty()) return 0D;

        double sum = 0;

        for (Number num : nums) sum += num.doubleValue();

        return nums.isEmpty() ? 0 : sum / nums.size();
    }
}