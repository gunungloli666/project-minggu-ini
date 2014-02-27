/**********************************************************************************************************************
 *  Ben Virost                                                                                                        *
 *  virostb@nku.edu                                                                                                   *
 *  CSC 494 Project                                                                                                   *
 *                                                                                                                    *
 *  Description:  Encodes/Decodes Affine, Autokey, Caesar, Gronsfeld, Keyword and Vigenere Ciphers                    *
 *                                                                                                                    *
 *  Currently, it does not have any cryptanalysis tools (such as something to look for encoded text in an affine      *
 *  cipher) or a GUI.  I'd like to change that in an update.                                                          *
 *                                                                                                                    *
 *                                                                                                                    *
 *                                                                                                                    *
 **********************************************************************************************************************/
import java.math.*;
import java.io.*;
import javax.swing.*;
import java.util.Random;

class MainProject
{
/*
 * Affine Cipher Methods
 */

    public static String AffineEncoder(String plaintext, int multKey, int addKey)
    {
        int len = plaintext.length(), asciiValue, newValue;
        String ciphertext = new String();
        char current;
        for (int i = 0; i < len; i++)
        {
            current = plaintext.charAt(i);
            asciiValue = ((int)current);

            if (Character.isSpace(current))
                ciphertext += ' ';
            else if (asciiValue > 90)
                asciiValue -= 32;

            if (asciiValue >= 65 && asciiValue <= 90)
            {
                newValue = (multKey * (asciiValue - 64)) + addKey;
                if (newValue > 26)
                    newValue = (newValue % 26);
                if (newValue == 0)
                    newValue = 26;
                ciphertext += (char)(newValue + 64);
            }
        }
        return ciphertext;

    }

    public static String AffineDecoder(String ciphertext, String multKey, int addKey)
    {
        BigInteger blah = new BigInteger(multKey);
        BigInteger modValue = new BigInteger("26");
        int len = ciphertext.length(), asciiValue, newValue;
        String plaintext = new String();
        char current;
        for (int i = 0; i < len; i++)
        {
            current = ciphertext.charAt(i);
            asciiValue = ((int)ciphertext.charAt(i));

            if (Character.isSpace(current))
                plaintext += ' ';
            else if (asciiValue < 97)
                asciiValue += 32;

            if (asciiValue >= 97 && asciiValue <= 122)
            {
                newValue = (blah.modInverse(modValue)).intValue()  * ((asciiValue - 96) - addKey);

                newValue %= 26;
                //if it equals 0, it's actually supposed to equal 26
                if (newValue == 0)
                    newValue = 26;
                //if it's less than 0, then it's merely 26 away
                if (newValue < 0)
                    newValue += 26;
                plaintext += (char)(newValue + 96);
            }
        }
        return plaintext;
    }

/*
 * Autokey Cipher Methods
 */

    //x is used to mark the place in the keyword because i increments no matter what the char is.  x only increments if it's a letter.
    public static String AutokeyEncoder(String plaintext, String keyword)
    {
        int len = plaintext.length(), asciiValue, newValue, letterValue, x = 0, counter = 0, nexti = 0;
        String ciphertext = new String();
        char current;
        plaintext = plaintext.toUpperCase(); //it makes it easier to have it all in one case
        keyword = keyword.toUpperCase();
        for (int i = 0; i < keyword.length(); i++)
        {
            current = plaintext.charAt(i);
            if (Character.isSpace(current))
            {
                ciphertext += ' ';
                i++;
                current = plaintext.charAt(i);
            }

            asciiValue = ((int)current);
            //if it's an uppercase letter, encode it
            if (asciiValue >= 65 && asciiValue <= 90)
            {
                letterValue = asciiValue - 65;
                newValue = letterValue + (((int)(keyword.charAt(counter))) - 65);
                newValue %= 26;
                ciphertext += (char)(newValue + 65);//add it to the ciphertext
                counter++;
            }
            nexti = i;
        }
        
        x = 0;
        for (int i = nexti + 1; i < len; i++)
        {
            char temp = ' ';
            current = plaintext.charAt(i);
            if (Character.isSpace(current))
            {
                ciphertext += ' ';
                i++;
                current = plaintext.charAt(i);
            }

            asciiValue = ((int)current);
            //if it's an uppercase letter, encode it
            if (asciiValue >= 65 && asciiValue <= 90)
            {
                letterValue = asciiValue - 65;
                temp = ciphertext.charAt(x);
                while (temp == ' ')
                {
                    x++;
                    temp = ciphertext.charAt(x);
                }

                newValue = letterValue + (((int)(temp)) - 65);//add the shift

                newValue %= 26;
                ciphertext += (char)(newValue + 65);//add it to the ciphertext
                x++;
            }

            temp = ' ';
        }
        return ciphertext;

    }

    public static String AutokeyDecoderForLength(String ciphertext, int keywordLength)
    {
        int x = 0, len = ciphertext.length(), asciiValue, newValue, letterValue, y = 0;
        String plaintext = new String();
        char current, temp = ' ';
        ciphertext = ciphertext.toLowerCase();//it makes it easier to have it all in one case

        for (int i = 0; i < keywordLength; i++)
        {
            if (x < ciphertext.length())
            {
                plaintext += ciphertext.charAt(x);
                x++;

                if (Character.isSpace(ciphertext.charAt(x - 1)))
                {
                    plaintext += ciphertext.charAt(x);
                    x++;
                }
                y = x;
            }
        }

        x = 0;
        for (int i = 0; i < (len - y); i++)
        {

            current = ciphertext.charAt(i + y);
            if (Character.isSpace(current))
                plaintext += ' ';


            else
            {
                asciiValue = ((int)current);
                if (asciiValue >= 97 && asciiValue <= 122)//if it's a lowercase letter, process it
                {
                    letterValue = asciiValue - 97;

                    temp = ciphertext.charAt(x);
                    while (temp == ' ')
                    {
                        x++;
                        temp = ciphertext.charAt(x);
                    }

                    newValue = letterValue - (((int)(temp)) - 97); //take off the shift
                    newValue %= 26;
                    //if we've gone below 0, we add 26, which has the effect of wrapping around to the end of the alphabet
                    if (newValue < 0)
                        newValue += 26;

                    plaintext += (char)(newValue + 97);//add it to the plaintext
                    x++;
                }
            }
            temp = ' ';
        }
        return plaintext;
    }

    public static String AutokeyDecoderForKeyword(String ciphertext, String keyword)
    {
        int x = 0, len = ciphertext.length(), asciiValue, newValue, letterValue, y = 0;
        String plaintext = new String();
        char current, temp = ' ';
        ciphertext = ciphertext.toLowerCase();//it makes it easier to have it all in one case
        keyword = keyword.toLowerCase();
        int keywordLength = keyword.length();

        for (int i = 0; i < keywordLength; i++)
        {
            current = ciphertext.charAt(x);
            if (Character.isSpace(current))
            {
                plaintext += ' ';
                x++;
                current = ciphertext.charAt(x);
            }

            asciiValue = ((int)current);
            if (asciiValue >= 97 && asciiValue <= 122)//if it's a lowercase letter, process it
            {
                letterValue = asciiValue - 97;
                newValue = letterValue - (((int)(keyword.charAt(i))) - 97); //take off the shift
                newValue %= 26;
                //if we've gone below 0, we add 26, which has the effect of wrapping around to the end of the alphabet
                if (newValue < 0)
                    newValue += 26;

                plaintext += (char)(newValue + 97);//add it to the plaintext
                x++;
            }
            y = x;
        }

        x = 0;
        for (int i = 0/*keywordLength*/; i < (len - y); i++)
        {

            current = ciphertext.charAt(i + y);// + keywordLength);
            if (Character.isSpace(current))
                plaintext += ' ';


            else
            {
                asciiValue = ((int)current);
                if (asciiValue >= 97 && asciiValue <= 122)//if it's a lowercase letter, process it
                {
                    letterValue = asciiValue - 97;

                    temp = ciphertext.charAt(x);
                    while (temp == ' ')
                    {
                        x++;
                        temp = ciphertext.charAt(x);
                    }

                    newValue = letterValue - (((int)(temp)) - 97); //take off the shift
                    newValue %= 26;
                    //if we've gone below 0, we add 26, which has the effect of wrapping around to the end of the alphabet
                    if (newValue < 0)
                        newValue += 26;

                    plaintext += (char)(newValue + 97);//add it to the plaintext
                    x++;
                }
            }
            temp = ' ';
        }
        return plaintext;
    }

/*
 *  Caesar Shift Cipher Methods
 */

    public static String CaesarEncoder(String plaintext, int shiftNumber)
    {
        int len = plaintext.length(), asciiValue, newValue;
        String ciphertext = new String();
        char current;
        for (int i = 0; i < len; i++)
        {
            current = plaintext.charAt(i);
            if (Character.isSpace(current))
                ciphertext += ' ';
            else
            {
                current = Character.toUpperCase(current);//ciphertext should be in uppercase
                asciiValue = ((int)current);

                //if it's an uppercase letter, encode it
                if (asciiValue >= 65 && asciiValue <= 90)
                {
                    newValue = asciiValue + shiftNumber;//add the shift
                    //if it's left the range of uppercase letters, subtract 26, which wraps it around to the beginning of the alphabet
                    if (newValue > 90)
                        newValue -= 26;
                    ciphertext += (char)newValue;//add it to the ciphertext
                }
            }
        }
        return ciphertext;

    }

    public static String CaesarDecoder(String ciphertext, int shiftNumber)
    {
        int len = ciphertext.length(), asciiValue, newValue;
        String plaintext = new String();
        char current;
        for (int i = 0; i < len; i++)
        {
            current = ciphertext.charAt(i);
            if (Character.isSpace(current))
                plaintext += ' ';

            else
            {
                current = Character.toLowerCase(current);//plaintext should be in lowercase
                asciiValue = ((int)current);

                if (asciiValue >= 97 && asciiValue <= 122)//if it's a lowercase letter, process it
                {
                    newValue = asciiValue - shiftNumber; //take off the shift
                    //if we've left the bound of lowercase letters, we add 26, which has the effect of wrapping around to the end of the alphabet
                    if (newValue < 97)
                        newValue += 26;
                    plaintext += (char)newValue;//add it to the plaintext
                }
            }
        }
        return plaintext;
    }

/*
 * Gronsfeld Methods
 */
    //x is used to mark the place in the keyword because i increments no matter what the char is.  x only increments if it's a letter.
    public static String GronsfeldEncoder(String plaintext)
    {
        int len = plaintext.length(), asciiValue, newValue, letterValue, x = 0;
        String ciphertext = new String();
        char currentLetter;
        int currentNumber;
        Random rand = new Random();
        plaintext = plaintext.toUpperCase(); //it makes it easier to have it all in one case

        for (int i = 0; i < len; i++)
        {
            currentLetter = plaintext.charAt(i);
            while (Character.isSpace(currentLetter))
            {
                ciphertext += ' ';
                i++;
                currentLetter = plaintext.charAt(i);
            }

            asciiValue = ((int)currentLetter);
            //if it's an uppercase letter, encode it
            if (asciiValue >= 65 && asciiValue <= 90)
            {
                letterValue = asciiValue - 65;
                currentNumber = rand.nextInt(10);
                newValue = letterValue + currentNumber;//add the shift

                newValue %= 26;
                ciphertext += (char)(newValue + 65);//add it to the ciphertext
                ciphertext += "" + currentNumber;
                x++;
            }
        }
        return ciphertext;

    }

    public static String GronsfeldDecoder(String ciphertext)
    {
        int x = 0, len = ciphertext.length(), asciiValue, newValue, letterValue;
        String plaintext = new String();
        char currentLetter;
        Character temp;
        int currentNumber;
        ciphertext = ciphertext.toLowerCase();//it makes it easier to have it all in one case

        for (int i = 0; i < len; i += 2)
        {
            currentLetter = ciphertext.charAt(i);
            while (Character.isSpace(currentLetter))
            {
                plaintext += ' ';
                i++;
                currentLetter = ciphertext.charAt(i);
            }
            temp = new Character(ciphertext.charAt(i + 1));
            currentNumber = Integer.parseInt(temp.toString());

            asciiValue = ((int)currentLetter);
            if (asciiValue >= 97 && asciiValue <= 122)//if it's a lowercase letter, process it
            {
                letterValue = asciiValue - 97;
                newValue = letterValue - currentNumber; //take off the shift

                //if we've gone below 0, we add 26, which has the effect of wrapping around to the end of the alphabet
                if (newValue < 0)
                    newValue += 26;

                plaintext += (char)(newValue + 97);//add it to the plaintext
                x++;
            }
        }
        return plaintext;
    }

/*
 * Keyword Cipher Methods
 */

    public static String KeywordEncoder(String plaintext, String keyword, int startingPlace)
    {
        char key[] = new char[26];
        int len = plaintext.length(), asciiValue, newValue;
        String ciphertext = new String();
        char current;
        key = EncodeKeyGenerator(keyword, startingPlace);
        for (int i = 0; i < len; i++)
        {
            current = plaintext.charAt(i);
            asciiValue = ((int)plaintext.charAt(i));

            if (Character.isSpace(current))
                ciphertext += ' ';
            else if (asciiValue > 90)
                asciiValue -= 32;

            if (asciiValue >= 65 && asciiValue <= 90)
                ciphertext += key[asciiValue - 65];
        }
        return ciphertext;

    }

    public static String KeywordDecoder(String ciphertext, String keyword, int startingPlace)
    {
        char key[] = new char[26];
        int len = ciphertext.length(), asciiValue, newValue, x = 0;
        String plaintext = new String();
        char current;

        ciphertext = ciphertext.toLowerCase();
        key = DecodeKeyGenerator(keyword, startingPlace);

        for (int i = 0; i < len; i++)
        {
            x = 0;
            current = ciphertext.charAt(i);
            if (Character.isSpace(current))
                plaintext += ' ';
            else
            {
                while (current != key[x] && x != 26)
                    x++;

                asciiValue = x + 97;

                plaintext += ((char)asciiValue);
            }
        }
        return plaintext;
    }

    //Generates your key in upper case letters
    public static char[] EncodeKeyGenerator(String keyword, int startingPlace)
    {
        keyword = keyword.toUpperCase();
        char[] alphabet = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
        char key[] = {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '};

        int j = startingPlace; //j represents the next place in the key array to place a letter
        for (int i = 0; i < keyword.length(); i++)
        {
            char current = keyword.charAt(i);
            //remove letter from Alphabet
            if (alphabet[((int)(current) - 65)] != ' ')
            {
                key[j] = current;
                alphabet[((int)(current) - 65)] = ' ';
                j = (j + 1) % 26;
            }
        }
        for (int i = 0; i < 26; i++)
        {
            if (alphabet[i] != ' ')
            {
                key[j] = alphabet[i];
                alphabet[i] = ' ';
                j = (j + 1) % 26;
            }
        }
        return key;
    }

    public static char[] DecodeKeyGenerator(String keyword, int startingPlace)
    {
        char alphabet[] = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
        char key[] = {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '};

        keyword = keyword.toLowerCase();        
        int j = startingPlace; //j represents the next place in the key array to place a letter
        for (int i = 0; i < keyword.length(); i++)
        {
            char current = keyword.charAt(i);
            //remove letter from Alphabet
            if (alphabet[((int)(current) - 97)] != ' ')
            {
                key[j] = current;
                alphabet[((int)(current) - 97)] = ' ';
                j = (j + 1) % 26;
            }
        }
        for (int i = 0; i < 26; i++)
        {
            if (alphabet[i] != ' ')
            {
                key[j] = alphabet[i];
                alphabet[i] = ' ';
                j = (j + 1) % 26;
            }
        }
        return key;
    }


/*
 *  Vigenere Cipher Methods
 */

    //x is used to mark the place in the keyword because i increments no matter what the char is.  x only increments if it's a letter.
    public static String VigenereEncoder(String plaintext, String keyword)
    {
        int len = plaintext.length(), asciiValue, newValue, letterValue, x = 0;
        String ciphertext = new String();
        char current;
        plaintext = plaintext.toUpperCase(); //it makes it easier to have it all in one case
        keyword = keyword.toUpperCase();
        for (int i = 0; i < len; i++)
        {
            current = plaintext.charAt(i);
            if (Character.isSpace(current))
                ciphertext += ' ';
            else
            {
                asciiValue = ((int)current);
                //if it's an uppercase letter, encode it
                if (asciiValue >= 65 && asciiValue <= 90)
                {
                    letterValue = asciiValue - 65;
                    newValue = letterValue + (((int)(keyword.charAt(x%(keyword.length())))) - 65);//add the shift

                    newValue %= 26;
                    ciphertext += (char)(newValue + 65);//add it to the ciphertext
                    x++;
                }
            }
        }
        return ciphertext;
    }

    public static String VigenereDecoder(String ciphertext, String keyword)
    {
        int x = 0, len = ciphertext.length(), asciiValue, newValue, letterValue;
        String plaintext = new String();
        char current;
        ciphertext = ciphertext.toLowerCase();//it makes it easier to have it all in one case
        keyword = keyword.toLowerCase();
        for (int i = 0; i < len; i++)
        {
            current = ciphertext.charAt(i);
            if (Character.isSpace(current))
                plaintext += ' ';

            else
            {
                asciiValue = ((int)current);
                if (asciiValue >= 97 && asciiValue <= 122)//if it's a lowercase letter, process it
                {
                    letterValue = asciiValue - 97;
                    newValue = letterValue - (((int)keyword.charAt(x%keyword.length())) - 97); //take off the shift
                    newValue %= 26;
                    //if we've gone below 0, we add 26, which has the effect of wrapping around to the end of the alphabet
                    if (newValue < 0)
                        newValue += 26;
                    plaintext += (char)(newValue + 97);//add it to the plaintext
                    x++;
                }
            }
        }
        return plaintext;
    }

/*
 * Main method
 */

    public static void main(String args[]) throws IOException
    {
        String ciphertext = new String(), plaintext = new String(), keyword = new String(), outputFilename = new String(), startingPlaceString;
        int encodeChoice, cipherChoice, repeat = 1, shiftNumber, i, multKey = 2, addKey, keywordLength, startingPlace;
        while (repeat == 1)
        {
            encodeChoice = Integer.parseInt(JOptionPane.showInputDialog("Enter your choice:\n1) Encode a message\n2) Decode a message"));
            outputFilename = JOptionPane.showInputDialog("Enter the filename for the output");
            FileWriter fw = new FileWriter(outputFilename + ".txt");
            BufferedWriter bw = new BufferedWriter(fw);
            cipherChoice = Integer.parseInt(JOptionPane.showInputDialog("Enter your choice:\n1) Affine Cipher\n2) Autokey Cipher\n3) Caesar Shift Cipher\n4) Gronsfeld Cipher\n5) Keyword Cipher\n6) Vigenere"));
            switch (encodeChoice)
            {
                case 1: //Encoding main methods
                    plaintext = JOptionPane.showInputDialog("Enter the plaintext: ");

                    switch (cipherChoice)
                    {
                        case 1: //Affine
                            while (multKey % 2 == 0 || multKey == 13)
                                multKey = Integer.parseInt(JOptionPane.showInputDialog("Enter the multiplicative key (between 0 and 26) you with to use to encode it: "));
                            addKey = Integer.parseInt(JOptionPane.showInputDialog("Enter the additive key you with to use to encode it: "));

                            ciphertext = AffineEncoder(plaintext, multKey, addKey);
                            System.out.println("Mult. key : " + multKey + "  Additive key: " + addKey + " " + ciphertext + "");
                            bw.write("Mult. key : " + multKey + "  Additive key: " + addKey + " " + ciphertext + "");
                        break;

                        case 2: //Autokey
                            keyword = JOptionPane.showInputDialog("Enter the keyword you would like to be placed at the beginning: ");

                            ciphertext = AutokeyEncoder(plaintext, keyword);
                            System.out.println("Key " + keyword + " : " + ciphertext + "");
                            bw.write("Encoded with key " + keyword + " : \n" + ciphertext + "");
                        break;

                        case 3: //Caesar
                            shiftNumber = Integer.parseInt(JOptionPane.showInputDialog("Enter the shift number(1 - 26) you would like to encode it with: "));

                            ciphertext = CaesarEncoder(plaintext, shiftNumber);
                            System.out.println("Key " + shiftNumber + " : " + ciphertext + "");
                            bw.write("Key " + shiftNumber + " : \n" + ciphertext + "");
                        break;

                        case 4: //Gronsfeld
                            ciphertext = GronsfeldEncoder(plaintext);
                            System.out.println("Ciphertext : " + ciphertext + "");
                            bw.write("Ciphertext : \n" + ciphertext + "");
                        break;

                        case 5: //Keyword
                            if (plaintext == null) //if cancel butten is hit
                                System.exit(0);

                            keyword = JOptionPane.showInputDialog("Enter the keyword you would like to encode it with: ");
                            if (keyword == null) //if cancel butten is hit
                                System.exit(0);

                            startingPlaceString = JOptionPane.showInputDialog("Enter where the keyword should start (0 - 25 or a-z): ");
                            if (startingPlaceString == null) //if cancel butten is hit
                                System.exit(0);

                            if (Character.isDigit(startingPlaceString.charAt(0)))
                                startingPlace = Integer.parseInt(startingPlaceString);
                            else
                                startingPlace = ((int)(startingPlaceString.charAt(0)) - 97);
                            ciphertext = KeywordEncoder(plaintext, keyword, startingPlace);
                            System.out.println("Key " + keyword + ":  " + ciphertext + "");
                        break;

                        case 6: //Vigenere
                            keyword = JOptionPane.showInputDialog("Enter the keyword you would like to encode it with: ");

                            ciphertext = VigenereEncoder(plaintext, keyword);
                            System.out.println("Key " + keyword + " : " + ciphertext + "");
                            bw.write("Key " + keyword + " : \n" + ciphertext + "");
                        break;
                    }
                break;

                case 2: //Decoding main methods
                    ciphertext = JOptionPane.showInputDialog("Enter the ciphertext: ");

                    switch (cipherChoice)
                    {
                        case 1: //Affine
                            String multKeyString = new String();

                            multKeyString = JOptionPane.showInputDialog("Enter the multiplicative key it was encoded with: ");
                            addKey = Integer.parseInt(JOptionPane.showInputDialog("Enter the additive key it was encoded with: "));

                            plaintext = AffineDecoder(ciphertext, multKeyString, addKey);
                            System.out.println("Mult. key : " + multKeyString + "  Additive key: " + addKey + " " + plaintext + "");
                            bw.write("Mult. key : " + multKeyString + "  Additive key: " + addKey + " " + plaintext + "");
                        break;

                        case 2: //Autokey
                            keyword = JOptionPane.showInputDialog("Enter the keyword, the length of the keyword that was used in the encoding or -1 to brute force it: ");

                            if (!Character.isLetter(keyword.charAt(0)))
                            {
                                keywordLength = Integer.parseInt(keyword);
                                if (keywordLength != -1)
                                {
                                    plaintext = AutokeyDecoderForLength(ciphertext, keywordLength);
                                    System.out.println("Key of length " + keywordLength + " : " + plaintext + "\n");
                                    bw.write("Key of length " + keywordLength + " : " + plaintext + "\n");
                                }
                                else
                                {
                                    for (keywordLength = 1; keywordLength < ciphertext.length(); keywordLength++) //start with the key equal to 1 and run through them
                                    {
                                        plaintext = AutokeyDecoderForLength(ciphertext, keywordLength);
                                        //Just some output formatting
                                        if (keywordLength >= 10)
                                        {
                                            System.out.println("Key of length " + keywordLength + " : " + plaintext + "\n");
                                            bw.write("Key of length " + keywordLength + " : " + plaintext + "\n");
                                        }
                                        else
                                            System.out.println("Key of length 0" + keywordLength + " : " + plaintext + "\n");
                                        //This is here to keep it easy to read.
                                        if (keywordLength % 5 == 0 && keywordLength != 0 && keywordLength != 25)
                                            JOptionPane.showMessageDialog(null, "Press Enter To Continue");
                                    }
                                }
                            }
                            else
                            {
                                plaintext = AutokeyDecoderForKeyword(ciphertext, keyword);
                                System.out.println("Key " + keyword + " : " + plaintext + "\n");
                                bw.write("Key " + keyword + " : " + plaintext + "\n");
                            }
                        break;

                        case 3: //Caesar
                            shiftNumber = Integer.parseInt(JOptionPane.showInputDialog("Enter the shift number(1 - 25) it was encoded with or enter -1 to run the alphabet: "));
                            if (shiftNumber != -1)
                            {
                                plaintext = CaesarDecoder(ciphertext, shiftNumber);//26 -
                                System.out.println("Key " + (shiftNumber) + " " + plaintext + "");
                            }
                            else
                            {
                                System.out.println("This will only print the first seventy characters to keep it easy to read");
                                for (shiftNumber = 1; shiftNumber < 26; shiftNumber++) //start with the key equal to 1 and run through them
                                {
                                    plaintext = CaesarDecoder(ciphertext, shiftNumber);

                                    //Just some output formatting
                                    if (shiftNumber >= 10)
                                    {
                                        System.out.print("Key " + shiftNumber + " : ");
                                        for (i = 0; i < 69; i++)
                                            System.out.print(plaintext.charAt(i));
                                        System.out.println(plaintext.charAt(69) + "\n");
                                        bw.write("Key " + shiftNumber + " : " + plaintext + "");
                                    }
                                    else
                                    {
                                        System.out.print("Key 0" + shiftNumber + " : ");
                                        for (i = 0; i < 69; i++)
                                            System.out.print(plaintext.charAt(i));
                                        System.out.println(plaintext.charAt(69) + "\n");
                                        bw.write("Key " + shiftNumber + " : " + plaintext + "");
                                    }
                                    //This is here to keep it easy to read.
                                    if (shiftNumber % 5 == 0 && shiftNumber != 0 && shiftNumber != 25)
                                        JOptionPane.showMessageDialog(null, "Press Enter To Continue");
                                }
                            }
                        break;

                        case 4: //Gronsfeld
                            plaintext = GronsfeldDecoder(ciphertext);
                            System.out.println("Plaintext: " + plaintext);
                            bw.write("Plaintext: " + plaintext);
                        break;

                        case 5: //Keyword
                            if (ciphertext == null) //if cancel butten is hit
                                System.exit(0);

                            keyword = JOptionPane.showInputDialog("Enter the keyword the message was encoded with: ");
                            if (keyword == null) //if cancel butten is hit
                                System.exit(0);

                            startingPlaceString = JOptionPane.showInputDialog("Enter where the keyword should start (0 - 25 or a-z): ");
                            if (startingPlaceString == null) //if cancel butten is hit
                                System.exit(0);

                            if (Character.isDigit(startingPlaceString.charAt(0)))
                                startingPlace = Integer.parseInt(startingPlaceString);
                            else
                                startingPlace = ((int)(startingPlaceString.charAt(0)) - 97);

                            plaintext = KeywordDecoder(ciphertext, keyword, startingPlace);
                            System.out.println("Key " + keyword + ":  " + plaintext + "");
                        break;

                        case 6: //Vigenere
                            keyword = JOptionPane.showInputDialog("Enter the keyword the message was encoded with: ");

                            plaintext = VigenereDecoder(ciphertext, keyword);
                            System.out.println("Key " + keyword + " : " + plaintext + "");
                            bw.write("Key " + keyword + " : \n" + plaintext + "");
                        break;
                    }
                break;
            }
            bw.close();
            fw.close();
            repeat = Integer.parseInt(JOptionPane.showInputDialog("Would you like to go again?"));
        }
        JOptionPane.showMessageDialog(null, "Project Completed.");
        System.exit(0);
    }
}
