import java.util.regex.Pattern;

public class PassChecker {

    public int solution(String s){
        Pattern lowerCase = Pattern.compile("[a-z]+");
        Pattern upperCase = Pattern.compile("[A-Z]+");
        Pattern digits = Pattern.compile("[0-9]+");

        //    We compute how many missing characters we have by checking if there is a match
        // in the string with out regex.
        int missingCh = 0;
        if (!lowerCase.matcher(s).find())
            missingCh += 1;
        if (!upperCase.matcher(s).find())
            missingCh += 1;
        if (!digits.matcher(s).find())
            missingCh += 1;

        if (s.length() < 6)
            /*  We know that if the length is < 6 the "repeating characters" problem will be solved by fixing
             either the length or the missing characters.
                We want to fix as many problems as possible, so we try to overlap the operations. Let's take an example.
            Missing characters=2 (uppercase, digit):
             •If we need to insert 1 or 2 characters to reach len=6 => we make the insertions to be exactly the characters we need.
             •If we need to insert more than 2 => we take the first 2 inserted to be our missing characters, then the rest
            can be anything.
            */
            return Math.max(missingCh, 6-s.length());
        else{
            /* Now we take the case there len >= 6.
               If 6 <= len <= 20 it's easy. We count how many repetitions we have that will give us the number of
             characters we need to replace and the missing characters, and we return the max out of them since we can
             replace any character with what we are missing.

               If len > 20 we have to count the repetitions.
               We keep in toDelete how many characters we need to delete, and we add them to changes since the
             actions of deleting them is required anyway.
             */
            int len = s.length();
            int toDelete = Math.max(0, s.length()-20);
            int changes = toDelete;
            int toReplace = 0;
            int[] repetitions = new int[len];

            /*
                Here we initialize an array that tells us the number of repeating characters that starts at the
              corresponding position in the string.
             */
            for (int i=0; i<len;){
                int j = i;
                while (i<len && s.charAt(i) == s.charAt(j))
                    i++;
                repetitions[j] = i-j;
            }

            /*
                Now, we're interested to minimize the operations we do on the string => we want to take advantage of
              the deletion we have to make to solve the repeating characters problem.
                We want to transform the numbers in our array in the form of 3m+2 because in this form we only need to
              replace m characters
             */
            for (int i=0; i<len && toDelete>0;i++){
                if (repetitions[i] < 3) continue;
                if (repetitions[i] % 3==0){
                    /*
                       If the number is a multiple of 3 we have to remove 1 => we also decrease 1 from toDelete
                    since we performed delete here.
                    */
                    repetitions[i] -= 1;
                    toDelete -= 1;
                }
            }

            for (int i=0; i<len && toDelete>0;i++){
                if (repetitions[i] < 3) continue;
                if (repetitions[i] % 3==1){
                    /*
                       If the number % 3 == 1 we have to remove 2, but we first check if we have more deletes to make.
                    */
                    repetitions[i] -= Math.min(2, toDelete);
                    toDelete -= 2;
                }
            }

            /*
                If by now we still have some deletes to make, we assign them in order to solve as much as possible
              of the repeating characters problem.
             */
            for (int i=0; i<len; i++){
                if (repetitions[i]>=3 && toDelete>0){  //while we still have deletes to make
                    int temp = repetitions[i]-2; //we make -2 to get to 3m (since we converted to 3m+2)
                    repetitions[i] -= toDelete;
                    toDelete -= temp;
                }
                if (repetitions[i]>=3)
                    /*
                       If we have no more deletes we just count how many we have to replace to fix our last problem.
                     */
                    toReplace += repetitions[i]/3;
            }
            /*
             At the end we return changes (how many we have to delete, 0 if none) + the maximum of the missing
           characters and the number of replacement we have to make.
            */
            return changes+Math.max(missingCh,toReplace);
        }
    }
}
