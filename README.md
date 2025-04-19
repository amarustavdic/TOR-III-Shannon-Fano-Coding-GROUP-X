## Shannon-Fano coding (book) - Group X

### Requirements

- [x] Find a pdf file of your favorite book and read the contents of the file into a byte array.
- [x] Change bytes to bits to obtain a representation of the pdf file as a string of bits.
- [x] Split the obtained sequence of bits into blocks of 16, and find the frequency of every possible block, and use the obtained frequencies to estimate the probability of all possible blocks of 16 bits.
- [x] Use the estimated probability distribution to obtain a Shannon-Fano code for the blocks of 16 bits.
- [x] Use the Shannon-Fano code to encode the bit representation of the pdf file of the book.
- [ ] Compare the size of the compressed file with the sizes of the same book in some other document formats (DjVu etc.).

### Additional Requirements (just for fun)

- Try making custom file such that you can compress any data and also alongside the compressed data save the dictionary that is needed for decompression.


### Structure of encoded file
```
+---------------------+
| Header (Codebook)   |
+---------------------+
| Encoded Bitstream   |
+---------------------+

Each codebook entry is encoded as:
+------------------------+
| 1 byte: block size     |
| N bytes: block bits    |
| 1 byte: codeword size  |
| M bytes: codeword bits |
+------------------------+
```
If you ask me this should be made like so, to have more realistic comparison, since without codebook, the encoded data is useless.
