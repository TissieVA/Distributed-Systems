package be.ua.fti.ei;

public class Hasher
{
    // public static int getHash(String input)
    // {
    //     long max = 2147483648L;
    //     long min = - 2147483648L;
    //     long hashed = input.hashCode();  //return 32 bit integer

    //     return (int) (((hashed+max)*32768)/(max+Math.abs(min)));
    // }

    public static int getHash(Object o)
    {
        // 32 bit
        int baseHash = o.hashCode();

        // Calculate sum of the 2 bit groups -> 17 bit
        int hash = (baseHash & 0xFFFF) + ((baseHash >> 16) & 0xFFFF);

        // Use leading bits to determine whether to flip the bits or not
        int leadingBits = hash >> 15;
        if((leadingBits & 0x1 ^ leadingBits >> 1) == 0x1)
            hash = hash ^ 0xFFFF;

        // Make sure 15 bits are returned
        return hash & 0x7FFF;
    }
}
