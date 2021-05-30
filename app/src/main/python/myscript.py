import pytesseract
import numpy as np

try:
    from PIL import Image
except ImportError:
    import Image

def main():
    return "Hello World..."

def name(name):
    return name

def ocr_core(fileimage):
    # text = pytesseract.image_to_string(Image.open(fileimage))
    text = fileimage
    return text

# print(ocr_core('/content/ImageTest/CM-1.jpg'))