# **HTTPS Simulator**

This project is an implementation of a secure communication simulator using cryptographic concepts. It was developed as part of a practical assignment, addressing key exchange with Diffie-Hellman and secure message exchange using the AES algorithm in CBC mode.

---

## **Features**

The simulator implements the following functionalities:

1. **Diffie-Hellman Key Generation**:
   - Generates a private value `a`.
   - Computes the public value `A = g^a mod p`.
   - Receives `B` (public value from the other participant).
   - Computes the shared key `V = B^a mod p`.
   - Derives the session key `S` as the 128 least significant bits of the SHA-256 hash of `V`.

2. **Secure Message Exchange**:
   - Decrypts messages in the format `[128-bit IV][encrypted message]`.
   - Reverses the content of the decrypted message.
   - Encrypts the reversed content with a new randomly generated IV in the format `[128-bit IV][encrypted message]`.

---

## **Code Architecture**

The project is organized into the following packages:

### **1. `config`**
Contains the `Constants` class, which stores constant values used throughout the project, such as `p`, `g`, AES configurations, and hashing algorithms.

### **2. `parts`**
- **`Parte1`**:
  Implements Diffie-Hellman key generation and related computations.
- **`Parte2`**:
  Implements encrypted message processing, including decryption, reversal, and re-encryption.

### **3. `security`**
Contains the `CryptographyUtils` class, which encapsulates cryptographic operations such as:
- Random IV generation.
- AES encryption and decryption.
- Data conversion to/from hexadecimal representation.

### **4. `utils`**
Contains the `FileUtils` class, which handles file reading and writing operations.

---

## **Requirements**

To run the project, you need:
- **Java JDK 17** or higher (JDK 21 recommended for modern features like multi-line strings).
- An editor or IDE (IntelliJ IDEA, Eclipse, etc.).
- Git for version control (optional).

---

## **How to Run**

### **1. Clone the Repository**
Clone this repository to your local machine:
```bash
git clone https://github.com/YOUR_USERNAME/HTTPS-Simulator.git
cd HTTPS-Simulator
```

### **2. Compile the Project**
Ensure that the JDK is properly configured. To compile the project, run:
```bash
javac -d out src/**/*.java
```

### **3. Run the Project**
To execute the project, use the following command:
```bash
java -cp out Main
```

### **4. Execution Flow**
The program prompts the user to choose between executing **Part 1** (Key Generation) or **Part 2** (Message Processing):

1. **Part 1**:
   - Generates `a` and `A`, saving them in `a.txt` and `AA.txt`.
   - Computes `S` after receiving `B` from the professor and saves it in `S.key`.

2. **Part 2**:
   - Decrypts the message stored in `message.enc`.
   - Reverses the content of the message.
   - Encrypts the reversed content with a new IV, saving it in `message_inverted.enc`.

---

## **Project Structure**

```plaintext
src/
├── config/
│   └── Constants.java        # Project constants (values for p, g, etc.)
├── parts/
│   ├── Parte1.java           # Diffie-Hellman key generation implementation
│   └── Parte2.java           # Secure message processing
├── security/
│   └── CryptographyUtils.java # Cryptographic operations (AES, hash, IV)
├── utils/
│   └── FileUtils.java        # File management utilities
└── Main.java                 # Main class to execute the program
```

---

## **Implementation Details**

### **Key Generation (Part 1)**
1. **Private Value `a`**:
   - Generated using `SecureRandom` and has a length smaller than `p`.

2. **Public Value `A`**:
   - Computed as `g^a mod p` and saved in `AA.txt`.

3. **Shared Key `V`**:
   - After receiving `B` (from the professor), computes `V = B^a mod p`.

4. **Session Key `S`**:
   - Derived from `SHA-256(V)` using the 128 least significant bits.

### **Message Exchange (Part 2)**
1. **Decryption**:
   - Uses AES in CBC mode with the IV extracted from the first 16 bytes of the message.

2. **Reversal**:
   - The decrypted text is reversed.

3. **Re-encryption**:
   - Generates a new random IV and encrypts the reversed message in the specified format.

---

## **Testing**

Ensure that the flow works as expected:
1. **Part 1**:
   - Verify that the values of `a` and `A` are correctly saved.
   - Validate the computation of `V` and `S`.

2. **Part 2**:
   - Test with various encrypted messages.
   - Ensure that the format `[IV][encrypted message]` is respected.

---

## **Common Issues**

1. **Invalid Key or IV**:
   - Ensure that `S.key` is correctly generated before running Part 2.

2. **File Conflicts**:
   - Delete or move existing files if you wish to restart the flow.

3. **Execution Errors**:
   - Confirm that the installed JDK is compatible (JDK 17 or higher).