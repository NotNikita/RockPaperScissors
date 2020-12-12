package com.company;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length < 3 || args.length % 2 == 0 || duplicates(args)){
            System.out.println("Incorrect number of arguments");
            return;
        }

        int pc_choice = (int)(Math.random() * args.length);
        String generated_key = generateSafeToken();
        String hmac = generateHmac(generated_key, args[pc_choice]);
        System.out.println(hmac);
        List<String> winners = new ArrayList<>((args.length - 1) / 2);
        List<String> loosers = new ArrayList<>((args.length - 1) / 2);
        getWinnersLoosers(winners,loosers, args, args.length, pc_choice, (args.length -1)/2);

        int counter = 1;
        Scanner input = new Scanner(System.in);
        while (true){
            for (String str : args) {
                System.out.println("\n" + counter +" - "+ str);
                counter++;
            }
            System.out.println("\n" + 0 +" - exit");
            System.out.print("Enter your move: ");
            int choice = input.nextInt();
            if (choice == 0){
                return;
            } else if (choice == pc_choice + 1){
                System.out.print("Tie");
                counter = 1;
                continue;
            } else if (choice > args.length || choice < 0){
                counter = 1;
                continue;
            }
            choice--;
            System.out.println("Your choice: " + args[choice]);
            System.out.println("Computer move: " + args[pc_choice]);
            getResult(args[choice], winners, loosers);
            System.out.println("Hmac key: " + generated_key);
            counter = 1;
        }

    }

    static boolean duplicates(final String[] args)
    {
        Set<String> lump = new HashSet<>();
        for (String i : args)
        {
            if (lump.contains(i)) return true;
            lump.add(i);
        }
        return false;
    }

    private static void getResult(String usr_choice, List<String> w, List<String> l) {
        if (w.contains(usr_choice))
            System.out.println("You WIN!");
        else if (l.contains(usr_choice))
            System.out.println("You Loose!");
    }

    public static void getWinnersLoosers(List<String> w, List<String> l, String[] a, int n, int ind, int amount){
        for (int i = ind + 1; i < ind + amount + 1; i++)
            w.add(a[(i % n)]);
        for (int i = ind + n - amount; i < ind + n; i++)
            l.add(a[(i % n)]);
    }

    private static String generateSafeToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return (bytesToHex(bytes));
    }

    private static String generateHmac(String key, String choice) throws Exception{
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        sha256_HMAC.init(new SecretKeySpec((key).getBytes(), "HmacSHA256"));
        byte[] result = sha256_HMAC.doFinal(choice.getBytes());
        return (bytesToHex(result));
    }

    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = (char) HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = (char) HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
