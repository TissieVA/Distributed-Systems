package be.ua.fti.ei;

public class Hasher
{

    public static int getHash(String input)
    {
        long max = 2147483648L;
        long min = - 2147483648L;
        long hashed = input.hashCode();  //return 32 bit integer

        return (int) (((hashed+max)*32768)/(max+Math.abs(min)));
    }
}
