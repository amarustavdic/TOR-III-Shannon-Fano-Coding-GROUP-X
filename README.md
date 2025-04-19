## Shannon-Fano coding (book) - Group X

### Requirements

- Find a pdf file of your favorite book and read the contents of the file into a byte array.
- Change bytes to bits to obtain a representation of the pdf file as a string of bits.
- Split the obtained sequence of bits into blocks of 16, and find the frequency of every possible block, and use the obtained frequencies to estimate the probability of all possible blocks of 16 bits.
- Use the estimated probability distribution to obtain a Shannon-Fano code for the blocks of 16 bits.
- Use the Shannon-Fano code to encode the bit representation of the pdf file of the book.
- Compare the size of the compressed file with the sizes of the same book in some other document formats (DjVu etc.).

### Additional Requirements (just for fun)

- Try making custom file such that you can compress any data and also alongside the compressed data save the dictionary that is needed for decompression.