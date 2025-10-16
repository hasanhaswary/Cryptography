import math
from math import ceil
from math import fmod
from math import tan
from scipy.stats import rankdata
import numpy as np

def ec_formula(row):
    return 1.148*(0.00720794*(ceil(abs(tan((-0.976665)-0.056824*row)-4.3663))-9.43194)+fmod(fmod(6.09373,(-3.89202)+row)*(row+0.0265342)+0.199477,-0.909621))

def generate_random_number_stream(seed, length):
    stream = [seed]

    for _ in range(length - 1):
        stream.append(ec_formula(stream[-1]))

    return stream

def generate_ec_strong_password(memorable_password,length):
    if length < len(memorable_password):
        raise ValueError("The length must be greater than or equal to the length of the memorable password.")
    
    # Convert memorable password into a numerical seed by summing normalized ASCII values
    seed = sum(ord(char)/256 for char in memorable_password)

    # Generate a random number stream based on the seed subject to a uniformly distributed transformation.
    random_stream = transform_to_uniform(generate_random_number_stream(seed, length))
                                         
    # Normalize the stream to printable ASCII characters
    strong_password = ''.join(chr(int(abs(value * 1000) % 94) + 33) for value in random_stream)

    return strong_password

def transform_to_uniform(input_array):
    """Transform array to uniform distribution"""
    input_array = np.array(input_array)
    ranks = rankdata(input_array, method='average')
    cdf_values = (ranks - 1) / (len(input_array) - 1)
    uniform_array = cdf_values
    return uniform_array

def main():
    print("EC-Based Password Generator with Random Stream")
    memorable_password = input("Enter a memorable password: ")
    length = int(input("Enter the desired length of the strong password: "))

    try:
        strong_password = generate_ec_strong_password(memorable_password, length)
        print(f"Strong password: {strong_password}")

        # Save the strong password to a file
        with open("ec_strong_password.txt", "w") as file:
            file.write(f"Memorable password: {memorable_password}\n")
            file.write(f"Strong password: {strong_password}\n")

        print("The strong password has been saved to 'ec_strong_password.txt'.")

    except ValueError as e:
        print(f"Error: {e}")


if __name__ == "__main__":
    main()