# Web Information Retreival - ex1

### Analysis

1. In our implementation there are 2 index data structures and another data structure that holds meta-data of the reviews.
The 2 index structures are implemented as one object class (dictionary), but used for different types of data:
    - Token dictionary: Has all the tokens of the reviews in the file compressed using k-1 to k front coding method, where our chosen k is 100.
It's implementation includes one large `String` that is an alphabetical concatenation of all the tokens of the reviews, where we discard a mutual prefix of each k-successive tokens.
In order to find the beginning and ending of a token, the dictionary also has 5 arrays:
        - `byte` Prefix sizes
        - `byte` Tokens length
        - `int` Tokens Frequency, where frequenct is calculated as the total number of appearances (including duplicates) of a token.
        - `int` Pointers to the starting location of tokens in the concatenated `String`. We only keep a pointer for 1 in k tokens.
        - `long` For each token we hold a pointer to the beginning of it's corresponding posting list (which is saved in another file on disk).
    - ProductID dictionary: Implemented the same way as above, except that in this case we hold product IDs of the reviews in the file, instead of tokens.
Frequency now represents the number of reviews that refer a given product ID.

    Each dictionary has a corresponding file holding it's elements' (token or product ID) posting lists.
    Those files encode for each element's posting list, using a group varint encoding, a sequence of numbers representing the review IDs where the element can found. 
    Due to the fact that review IDs are in an ascending order, we encode them as the gap difference rather than the actual ID.
    As for the tokens posting list, we also encode for each token (using a group varint encoding) a corresponding list of frequencies of the token in each review ID that it appears in.
    
    Moving on, our third structure that holds the review meta-data is implemented using 4 arrays and a `String`.
    The arrays are of a size equals to the number of reviews holding:
    - `byte` for review scores.
    - `short` for each of numerator and denominator of review helpfulness.
    - `short` for the number of tokens in each review.
    
    The `String` is a concatenation of the product IDs of all reviews in ascending order of review ID, so instead of holding an array of product IDs (`String`s) we only hold one `String` and minimized the memory overhead of `String`s.
    
    ![Image of Diagram](https://raw.githubusercontent.com/shahafhermann/Web-Information-Retreival-ex1/master/%D7%93%D7%99%D7%90%D7%92%D7%A8%D7%9E%D7%94.jpg?token=AIU6RGA3IWZ3SQGTPF2JYBK6X2L5U)

2. When creating an IndexReader, we load into main memory both dictionaries and the review meta-data, but keep storing the posting lists for both dictionaries on disk.

3. Let's denote `n = number of reviews`, we have:
    - The size of the table of review data is: ![](https://latex.codecogs.com/svg.latex?%5Cinline%20n%5Ccdot%5Cleft%28short&plus;short&plus;short&plus;byte%5Cright%29%3D7n) `byte`s.
    The `String` of the review meta-data has exactly ![](https://latex.codecogs.com/svg.latex?%5Cinline%2010%5Ccdot%20n) `char`s (assuming each product ID is composed of 10 letters).
    Overhead of a `String` in Java has `4B` for the pointer of the array of chars, `8B` header, `4B` offset, `4B` length of the `String`, `4B` hashcode, the `char` array is of size: `8B` header, `4B` length and ![](https://latex.codecogs.com/svg.latex?%5Cinline%2010%5Ccdot%20n%5Ccdot%20sizeOf%5Cleft%28char%5Cright%29) padding. Together it sums to ![](https://latex.codecogs.com/svg.latex?%5Cinline%20%5Csim%2836&plus;20%5Ccdot%20n%29) `byte`s.
   
    Let's denote `t = number of tokens`, we have:
    - ![](https://latex.codecogs.com/svg.latex?%5Cinline%20t%5Ccdot%5Cleft%28int&plus;byte&plus;byte&plus;long%5Cright%29&plus;%5Cleft%28%5Cfrac%7Bt%7D%7BK%7D%5Cright%29%5Ccdot%20int%3Dt%5Ccdot%5Cleft%284&plus;1&plus;1&plus;8%5Cright%29&plus;%5Cleft%28%5Cfrac%7Bt%7D%7B100%7D%5Cright%29%5Ccdot4), and overall ![](https://latex.codecogs.com/svg.latex?%5Cinline%2014%5Ccdot%20t&plus;%5Cfrac%7Bt%7D%7B25%7D) `byte`s for the tables.
     
    Let's denote `d = average size of token`, `s = average size of suffix without mutual prefix`, we have:
    - The concatenated `String` has `36B` overhead plus ![](https://latex.codecogs.com/svg.latex?%5Cinline%20%5Cleft%28%5Cleft%28%5Cfrac%7Bt%7D%7BK%7D%5Cright%29%5Ccdot%20d&plus;%5Cleft%28t-%5Cfrac%7Bt%7D%7BK%7D%5Cright%29%5Ccdot%20s%5Cright%29%5Ccdot%20char), and overall ![](https://latex.codecogs.com/svg.latex?%5Cinline%2036&plus;%5Cleft%28%5Cfrac%7Bt%7D%7B100%7D%5Ccdot%20d&plus;%5Cleft%28t-%5Cfrac%7Bt%7D%7B100%7D%5Cright%29%5Ccdot%20s%5Cright%29%5Ccdot2) `byte`s.
    
    As for the posting list file, it's very hard to give a theoretical analysis in association with the size of memory it will require, 
    because it depends on each the actual numbers we're encoding.
    Overall we can say that we expect the encoded posting list to be much smaller than the actual size that the numbers would take without encoding, 
    due to the fact that we encode gap differences rather than actual numbers, and the fact that group varint doesn't waste bytes for padding or a fixed length for representation.