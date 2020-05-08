# Web-Information-Retreival-ex1

Analysis:

1. In our implementation there are 2 index data structures and another data structure that holds meta-data of the reviews.
The 2 index structures are implemented as one object class (dictionary), but used for different types of data:
- Token dictionary: Has all the tokens of the reviews in the file compressed using k-1 to k compression method, where our chosen k is 100.
It's implementation includes one large String that is an alphabetical concatenation of all the tokens of the reviews, where we discard a mutual prefix of each k-successive tokens.
In order to find the beginning and ending of a token, the dictionary also has 4 arrays:
- Prefix sizes
    - Tokens length
    - Tokens Frequency
    - Pointers to the starting location of tokens in the concatenated String. We only keep a pointer for 1 in k tokens.
- ProductID dictionary