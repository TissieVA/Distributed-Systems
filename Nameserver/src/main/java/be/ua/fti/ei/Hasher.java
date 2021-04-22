package be.ua.fti.ei;

public class Hasher
{

    public static int getHash(String input)
    {
        int hashed = input.hashCode();  //return 32 bit integer
        int hashed_first16 = (hashed & 0b11111111111111110000000000000000) >>  16;
        int hashed_last16  = hashed & 0b1111111111111111;

        int output = hashed_first16 ^ hashed_last16; //XOR both
        return output & 0b111111111111111;
    }
}
