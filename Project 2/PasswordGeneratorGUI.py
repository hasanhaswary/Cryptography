import tkinter as tk
from tkinter import ttk, messagebox, filedialog
import math
from math import ceil, fmod, tan
from scipy.stats import rankdata
import numpy as np

class PasswordGeneratorGUI:
    def __init__(self, root):
        self.root = root
        self.root.title("EC-Based Strong Password Generator")
        self.root.geometry("500x500")
        self.root.resizable(False, False)
        self.root.configure(bg='#f0f0f0')
        
        self.setup_styles()
        self.create_widgets()
        
    def setup_styles(self):
        style = ttk.Style()
        style.theme_use('clam')
        
        # Configure styles
        style.configure('Title.TLabel', 
                       font=('Arial', 16, 'bold'), 
                       background='#f0f0f0',
                       foreground='#2c3e50')
        
        style.configure('Subtitle.TLabel',
                       font=('Arial', 10),
                       background='#f0f0f0',
                       foreground='#666666')
        
        style.configure('Custom.TButton',
                       font=('Arial', 10, 'bold'),
                       padding=(10, 6))
        
        style.configure('Clear.TButton',
                       font=('Arial', 10, 'bold'),
                       padding=(10, 6),
                       background='#e74c3c',
                       foreground='white')

    def create_widgets(self):
        # Main container
        main_frame = ttk.Frame(self.root, padding="20")
        main_frame.pack(fill=tk.BOTH, expand=True)
        
        # Title Section
        title_label = ttk.Label(main_frame, 
                               text="EC-Based Strong Password Generator", 
                               style='Title.TLabel')
        title_label.pack(pady=(0, 5))
        
        subtitle_label = ttk.Label(main_frame,
                                  text="Generate cryptographically strong passwords using Evolutionary Computing",
                                  style='Subtitle.TLabel')
        subtitle_label.pack(pady=(0, 20))
        
        # Password Configuration Frame
        config_frame = ttk.LabelFrame(main_frame, text="Password Configuration", padding="15")
        config_frame.pack(fill=tk.X, pady=(0, 15))
        
        # Memorable Password
        memorable_frame = ttk.Frame(config_frame)
        memorable_frame.pack(fill=tk.X, pady=(0, 10))
        
        ttk.Label(memorable_frame, text="Memorable Password:", 
                 font=('Arial', 10)).pack(side=tk.LEFT)
        
        self.memorable_var = tk.StringVar()
        self.memorable_entry = ttk.Entry(memorable_frame, 
                                        textvariable=self.memorable_var,
                                        width=25,
                                        font=('Arial', 10))
        self.memorable_entry.pack(side=tk.LEFT, padx=(10, 0))
        # Removed pre-populated data
        
        # Desired Length
        length_frame = ttk.Frame(config_frame)
        length_frame.pack(fill=tk.X, pady=(0, 10))
        
        ttk.Label(length_frame, text="Desired Length:", 
                 font=('Arial', 10)).pack(side=tk.LEFT)
        
        self.length_var = tk.StringVar(value="16")  # Default but empty display
        self.length_entry = ttk.Entry(length_frame, 
                                     textvariable=self.length_var,
                                     width=5,
                                     font=('Arial', 10),
                                     justify=tk.CENTER)
        self.length_entry.pack(side=tk.LEFT, padx=(10, 5))
        
        ttk.Label(length_frame, text="(8-32)").pack(side=tk.LEFT)
        
        # Action Buttons Frame
        button_frame = ttk.Frame(main_frame)
        button_frame.pack(fill=tk.X, pady=15)
        
        # Generate and Clear buttons side by side
        left_button_frame = ttk.Frame(button_frame)
        left_button_frame.pack(side=tk.LEFT)
        
        self.generate_btn = ttk.Button(left_button_frame,
                                      text="Generate Password",
                                      command=self.generate_password,
                                      style='Custom.TButton')
        self.generate_btn.pack(side=tk.LEFT, padx=(0, 10))
        
        self.clear_btn = ttk.Button(left_button_frame,
                                   text="Clear All",
                                   command=self.clear_all,
                                   style='Clear.TButton')
        self.clear_btn.pack(side=tk.LEFT)
        
        # Generated Password Section
        output_frame = ttk.LabelFrame(main_frame, text="Generated Strong Password", padding="15")
        output_frame.pack(fill=tk.BOTH, expand=True, pady=(10, 0))
        
        # Password Display
        self.password_var = tk.StringVar()
        password_display_frame = ttk.Frame(output_frame)
        password_display_frame.pack(fill=tk.X, pady=(0, 15))
        
        self.password_entry = ttk.Entry(password_display_frame,
                                       textvariable=self.password_var,
                                       state='readonly',
                                       font=('Courier', 12, 'bold'),
                                       foreground='#2c3e50',
                                       width=40)
        self.password_entry.pack(fill=tk.X, padx=5)
        
        # Action Buttons Frame (Copy and Save)
        action_button_frame = ttk.Frame(output_frame)
        action_button_frame.pack(fill=tk.X)
        
        self.copy_btn = ttk.Button(action_button_frame,
                                  text="Copy to Clipboard",
                                  command=self.copy_to_clipboard,
                                  style='Custom.TButton')
        self.copy_btn.pack(side=tk.LEFT, padx=(0, 10))
        
        self.save_btn = ttk.Button(action_button_frame,
                                  text="Save to File",
                                  command=self.save_to_file,
                                  style='Custom.TButton')
        self.save_btn.pack(side=tk.LEFT)

    # Your EC Functions
    def ec_formula(self, row):
        return 1.148*(0.00720794*(ceil(abs(tan((-0.976665)-0.056824*row)-4.3663))-9.43194)+fmod(fmod(6.09373,(-3.89202)+row)*(row+0.0265342)+0.199477,-0.909621))

    def generate_random_number_stream(self, seed, length):
        stream = [seed]
        for _ in range(length - 1):
            stream.append(self.ec_formula(stream[-1]))
        return stream

    def generate_ec_strong_password(self, memorable_password, length):
        if length < len(memorable_password):
            raise ValueError("The length must be greater than or equal to the length of the memorable password.")
        
        seed = sum(ord(char)/256 for char in memorable_password)
        random_stream = self.transform_to_uniform(self.generate_random_number_stream(seed, length))
        strong_password = ''.join(chr(int(abs(value * 1000) % 94) + 33) for value in random_stream)
        return strong_password

    def transform_to_uniform(self, input_array):
        input_array = np.array(input_array)
        ranks = rankdata(input_array, method='average')
        cdf_values = (ranks - 1) / (len(input_array) - 1)
        uniform_array = cdf_values
        return uniform_array

    def generate_password(self):
        memorable_password = self.memorable_var.get().strip()
        length_str = self.length_var.get().strip()
        
        if not memorable_password:
            messagebox.showerror("Error", "Please enter a memorable password")
            return
        
        if not length_str:
            messagebox.showerror("Error", "Please enter a password length")
            return
            
        if not length_str.isdigit():
            messagebox.showerror("Error", "Please enter a valid number for password length")
            return
        
        length = int(length_str)
        
        if length < 8 or length > 32:
            messagebox.showerror("Error", "Password length must be between 8 and 32 characters")
            return
        
        try:
            # Show loading state
            self.generate_btn.config(state='disabled')
            self.root.update()
            
            strong_password = self.generate_ec_strong_password(memorable_password, length)
            self.password_var.set(strong_password)
            messagebox.showinfo("Success", "Strong password generated successfully!")
            
        except Exception as e:
            messagebox.showerror("Error", f"Error generating password: {e}")
        finally:
            self.generate_btn.config(state='normal')

    def copy_to_clipboard(self):
        password = self.password_var.get()
        if password:
            self.root.clipboard_clear()
            self.root.clipboard_append(password)
            messagebox.showinfo("Success", "Password copied to clipboard!")
        else:
            messagebox.showwarning("Warning", "No password to copy. Please generate a password first.")

    def save_to_file(self):
        password = self.password_var.get()
        memorable_password = self.memorable_var.get()
        
        if not password:
            messagebox.showwarning("Warning", "No password to save. Please generate a password first.")
            return
        
        try:
            filename = filedialog.asksaveasfilename(
                defaultextension=".txt",
                filetypes=[("Text files", "*.txt"), ("All files", "*.*")],
                title="Save Strong Password",
                initialfile="strong_password.txt"
            )
            
            if filename:
                with open(filename, "w") as file:
                    file.write("EC-Based Strong Password Generator\n")
                    file.write("=" * 40 + "\n")
                    file.write(f"Memorable password: {memorable_password}\n")
                    file.write(f"Strong password: {password}\n")
                    file.write(f"Length: {len(password)}\n")
                    file.write("\nGenerated using Evolutionary Computing Cipher\n")
                
                messagebox.showinfo("Success", f"Password saved to {filename}")
                
        except Exception as e:
            messagebox.showerror("Error", f"Failed to save file: {e}")

    def clear_all(self):
        """Clear all input fields and generated password"""
        self.memorable_var.set("")
        self.length_var.set("16")  # Reset to default length
        self.password_var.set("")
        self.memorable_entry.focus()  # Set focus back to memorable password field
        messagebox.showinfo("Cleared", "All fields have been cleared!")

def main():
    root = tk.Tk()
    app = PasswordGeneratorGUI(root)
    root.mainloop()

if __name__ == "__main__":
    main()