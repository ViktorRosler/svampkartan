from tflite_model_maker import image_classifier
from tflite_model_maker.image_classifier import DataLoader

import os
dir = os.path.dirname(__file__)
data_path = os.path.join(dir, 'ML_DATA')
model_path2 = os.path.join(dir, 'Model')

# Load input data specific to an on-device ML app.
data = DataLoader.from_folder(data_path)
train_data, test_data = data.split(0.9)

# Customize the TensorFlow model.
model = image_classifier.create(train_data, epochs=10)

# Evaluate the model.
loss, accuracy = model.evaluate(test_data)

# Export to Tensorflow Lite model and label file in export_dir.
model.export(export_dir=model_path)


input()

