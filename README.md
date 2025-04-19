## Shannon-Fano coding (book) - Group X


This project demonstrates a custom implementation of Shannon-Fano coding to 
compress the contents of a PDF book file. The focus is on encoding blocks of 
bits based on their observed frequency and generating an efficient, prefix-free 
code using Shannon-Fano's divide-and-conquer approach.

---

### Requirements
These tasks reflect the official project requirements:
- [x] **Read a PDF file** of your favorite book and load its binary content into a byte array.
- [x] **Convert bytes to a bitstream** to represent the file as a continuous string of bits.
- [x] **Split the bitstream into blocks** of 16 bits. Count frequencies of all distinct blocks and estimate the empirical probability distribution.
- [x] Use the estimated probability distribution to **obtain a Shannon-Fano code** for the blocks of 16 bits.
- [x] Use the Shannon-Fano code to **encode the bit representation** of the pdf file of the book.
- [ ] **Compare** the size of the compressed file with the sizes of the same book in some other document formats (DjVu etc.).

---

### Additional Features (4FUN)
- [x] Implemented a custom file format that includes:
    - The encoded data stream.
    - A complete embedded codebook, to make decoding possible too (and also more realistic size comparison against other compression algorithms).

---

### Encode File Structure

To ensure the compressed file can be decompressed without any external metadata, our format stores both the codebook and the encoded data.

```
+---------------------+
| Header (Codebook)   |
+---------------------+
| Encoded Bitstream   |
+---------------------+
```

**Codebook Structure (per block):**
Each codebook entry stores the mapping between an original bit block and its assigned codeword:

```
+------------------------+
| 1 byte: block size     |
| N bytes: block bits    |
| 1 byte: codeword size  |
| M bytes: codeword bits |
+------------------------+
```

- The number of entries in the codebook is written at the start as a 4-byte integer.
- All sizes are in bits, but packed efficiently into bytes.
- This flexible layout supports different block sizes and variable-length codewords.

**Why include the codebook?**
Including the codebook ensures that the compressed file is fully self-contained and can be decompressed without relying on external references — essential for fair compression comparisons and real-world use cases.

---

### Compression Outcome

For a realistic test case, we used an 800-page `.pdf` book (approx. 52.8 MB). Results:

**Original Size**: 52,851,768 bytes

**Compressed Size**: 50,105,466 bytes

**Difference**: ≈2.7 MB saved

