package arollavengers.core.util;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public enum ShuffleAlgorithm {
    /**
     * <blockquote>
     * <p>
     * A computer is capable of generating a "perfect shuffle", a random permutation of the cards.
     * The Fisher–Yates shuffle, popularized by Donald Knuth, is a simple (a few lines of code)
     * and efficient (O(n) on an n-card deck, assuming constant time for fundamental steps) algorithm
     * for doing this.
     * </p>
     * <small><a href="http://en.wikipedia.org/wiki/Shuffling">Shuffling Wikipedia</a></small>
     * </blockquote>
     */
    Random
            {
                @Override
                public <T> void shuffle(List<T> list) {
                    Collections.shuffle(list, new SecureRandom());
                }
            },
    /**
     * <blockquote>
     * <p>
     * Computationally, you can shuffle a deck by generating a permutation of the set 1:n,
     * but that is not how real cards are shuffled.
     * </p>
     * <p>
     * The riffle (or "dovetail") shuffle is the most common shuffling algorithm.
     * A deck of n cards is split into two parts and the two stacks are interleaved.
     * The GSR (Gilbert-Shannon-Reeds) algorithm simulates this physical process.
     * </p>
     * <small><a href="http://blogs.sas.com/content/iml/2011/04/13/a-statistical-model-of-card-shuffling/">A statistical model of card shuffling</a></small>
     * </blockquote>
     * <p/>
     * <p>
     * Current implementation is based on algorithm described in
     * <a href="http://blogs.sas.com/content/iml/2011/04/20/an-improved-simulation-of-card-shuffling/">An improved simulation of card shuffling</a>
     * </p>
     */
    GSR
            {
                @Override
                public <T> void shuffle(List<T> list) {
                    shuffle(list, 1);
                }

                @Override
                public <T> void shuffle(List<T> list, int nbShuffles) {
                    class Tuple implements Comparable<Tuple> {
                        int index;
                        double y;

                        public Tuple() {
                        }

                        @Override
                        public int compareTo(Tuple o) {
                            if (y > o.y) {
                                return 1;
                            }
                            else if (y < o.y) {
                                return -1;
                            }
                            return 0;
                        }
                    }

                    SecureRandom random = new SecureRandom();
                    Tuple[] tuples = new Tuple[list.size()];
                    for (int i = 0; i < tuples.length; i++) {
                        tuples[i] = new Tuple();
                    }
                    Object[] elements = new Object[list.size()];

                    for(int k=0;k<nbShuffles;k++) {
                        // 1. Generate n random points from the uniform distribution on [0,1]...
                        for (Tuple tuple : tuples) {
                            tuple.y = random.nextDouble();
                        }

                        // ...and label them in order x1 < x2 < ... < xn.
                        Arrays.sort(tuples);

                        for (int i = 0; i < tuples.length; i++) {
                            tuples[i].index = i;
                            // 2. Transform these points under the mapping y = 2x mod 1.
                            double y = tuples[i].y;
                            tuples[i].y = (2.0 * y) % 1.0;
                        }

                        // sort them according to y
                        Arrays.sort(tuples);

                        elements = list.toArray(elements);
                        for (int i = 0; i < tuples.length; i++) {
                            //noinspection unchecked
                            list.set(i, (T) elements[tuples[i].index]);
                        }
                    }
                }
            };


    public abstract <T> void shuffle(List<T> list);

    /**
     * Indicate the number of shuffle to perform.<br/>
     * <blockquote><p>
     * <a href="http://projecteuclid.org/euclid.aoap/1177005705">Bayer and Diaconis (1992)</a>
     * describes how many shuffles are needed to randomize a deck of cards.
     * Their famous result that it takes seven shuffles to randomize a 52-card
     * deck is known as "the bane of bridge players" because the result
     * motivated many bridge clubs to switch from hand shuffling to computer
     * generated shuffling.
     * </p>
     * <small><a href="http://blogs.sas.com/content/iml/2011/04/13/a-statistical-model-of-card-shuffling/">A statistical model of card shuffling</a></small>
     * </blockquote>
     * <p>
     * Default implementation is a simple loop that call
     * {@link #shuffle(java.util.List)} <code>nbShuffles</code> times.
     * </p>
     * <p>
     * Implementation can override this method to make it more efficient,
     * such as intermediate datastructure reuse.
     * </p>
     *
     * @param list list to shuffle
     * @param nbShuffles number of shuffle to perform
     */
    public <T> void shuffle(List<T> list, int nbShuffles) {
        for (int i = 0; i < nbShuffles; i++) {
            shuffle(list);
        }
    }

}
