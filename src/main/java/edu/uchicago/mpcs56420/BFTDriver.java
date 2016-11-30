package edu.uchicago.mpcs56420;

import edu.uchicago.mpcs56420.BloomFilterTrie.BloomFilterTrie;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Created by Anuved on 11/29/2016.
 */
public class BFTDriver {
    public static void main(String[] args) {

        /* Hard-coded input and query directories for quick testing */
//        File dbFolder = new File("db/test-med");
//        File dbFolder = new File("db/test-large");
//        File dbFolder = new File("db/test-xlarge");
        File dbFolder = new File(args[0]);
//        File queryFolder = new File("query/test-1");
        File queryFolder = new File(args[1]);


        /* Initialize BFT and benchmark data structures */
        BloomFilterTrie bloomFilterTrie = new BloomFilterTrie(63, 9);

        try {
            System.out.println("Populating Bloom Filter Trie....");
            populateBFT(dbFolder, bloomFilterTrie);
            System.out.println("Successfully populated. Reading queries...");

            ArrayList<String> queries = readQueries(queryFolder);
            for(String query : queries) {
                System.out.println("Query: " + query);
                boolean found = bloomFilterTrie.containsKmer(query);
                if(found) {
                    System.out.println("Query k-mer found in following genomes:");
                    ArrayList<String> genomesContainingKmer = bloomFilterTrie.genomesContainingKmer(query);
                    for (int i = 0; i < genomesContainingKmer.size(); i++) {
                        System.out.println(genomesContainingKmer.get(i));
                    }
                } else System.out.println("Query k-mer not found in any genomes.");
                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Method to populates BFT */
    private static void populateBFT(File folder, BloomFilterTrie bft) throws IOException {
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                bft.insertSequence(file);
            }
        }
    }

    /* Method to read queries */
    private static ArrayList<String> readQueries(File folder) throws IOException {
        ArrayList<String> queries = new ArrayList<>();

        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));

                String line;
                StringBuilder query = new StringBuilder();
                while ((line = inputReader.readLine()) != null)
                    query.append(line);

                queries.add(query.toString());

            }
        }

        return queries;
    }

}
