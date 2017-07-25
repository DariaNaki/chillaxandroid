package exampfi.daria.chillaxlinuxtryandroid;

/**
 * Created by Ira on 11/9/2016.
 */

public enum ChillaxEnum
{
    MIN_LEGAL_AGE(18), MAX_USER_CASHED_COUNT(10);

    private  int value;

    private ChillaxEnum(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}