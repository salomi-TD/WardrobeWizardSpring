<!DOCTYPE html>
<html>
<head>
    <title>Image Upload and Camera Capture</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f0f0f0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .container {
            background-color: white;
            border-radius: 5px;
            padding: 20px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            text-align: center;
        }

        h1 {
            background-color: #333;
            color: white;
            padding: 10px;
            border-radius: 5px 5px 0 0;
            margin: 0;
        }

        .form-container {
            margin-top: 20px;
        }

        #camera-feed {
            width: 100%;
            max-width: 640px;
            margin: 10px 0;
        }

        input[type="file"] {
            display: none;
        }

        label {
            background-color: #333;
            color: white;
            padding: 10px 20px;
            border-radius: 5px;
            cursor: pointer;
        }

        button {
            background-color: #333;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        button:disabled {
            background-color: #aaa;
            cursor: not-allowed;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Image Upload and Camera Capture</h1>

        <div class="form-container">
            <h2>Image Upload</h2>
            <form id="imageUploadForm" enctype="multipart/form-data">
                <input type="file" id="imageInput" name="imageFile" accept="image/*">
                <label for="imageInput">Choose Image</label>
                <button type="button" onclick="uploadImage()">Upload</button>
            </form>
            <div id="result"></div>
        </div>

        <div class="form-container">
            <h2>Camera Capture</h2>
            <video id="camera-feed" autoplay></video>
            <button id="capture-button">Capture</button>
            <form id="upload-form" action="/upload" method="POST" enctype="multipart/form-data">
                <canvas id="image-canvas" style="display: none;"></canvas>
                <button type="submit" id="upload-button" style="display: none;">Capture and Upload</button>
            </form>
        </div>

        <div class="form-container">
            <h2>Process Image</h2>
            <form id="process-form" action="/test" method="GET" enctype="multipart/form-data">
                <button type="submit" id="process-button" disabled>Process</button>
            </form>
        </div>
    </div>
    <script>
        const cameraFeed = document.getElementById('camera-feed');
        const captureButton = document.getElementById('capture-button');
        const imageCanvas = document.getElementById('image-canvas');
        const uploadButton = document.getElementById('upload-button');
        const processButton = document.getElementById('process-button');

        // Access the user's camera
        navigator.mediaDevices.getUserMedia({ video: true })
            .then(function (stream) {
                cameraFeed.srcObject = stream;
            })
            .catch(function (error) {
                console.error('Error accessing the camera: ' + error);
            });

        // Capture an image from the camera
        captureButton.addEventListener('click', function () {
            const context = imageCanvas.getContext('2d');
            imageCanvas.width = cameraFeed.videoWidth;
            imageCanvas.height = cameraFeed.videoHeight;
            context.drawImage(cameraFeed, 0, 0, imageCanvas.width, imageCanvas.height);

            // Show the upload button
            uploadButton.style.display = 'block';

            // Prevent the default form submission
            event.preventDefault();
        });

        // Handle form submission
        document.getElementById('upload-form').addEventListener('submit', function (e) {
            e.preventDefault(); // Prevent the default form submission

            // Convert the canvas image to base64 data URL
            const imageData = imageCanvas.toDataURL('image/jpeg');

            // Create a FormData object and append the captured image
            const formData = new FormData();
            formData.append('imageFile', dataURItoBlob(imageData));

            // Send the captured image to the server using Fetch API
            fetch('/upload', {
                method: 'POST',
                body: formData
            })
            .then(response => response.json())
            .then(data => {
                // Handle the response from the server (e.g., display results)
                if (data.error) {
                    console.error('Server error:', data.error);
                } else {
                    console.log('Server response:', data.message);

                    // Enable the "Process" button
                    processButton.disabled = false;
                } 
            })
            .catch(error => {
                console.error('Error sending image data:', error);
            });
        });

        // Function to convert data URI to Blob
        function dataURItoBlob(dataURI) {
            const byteString = atob(dataURI.split(',')[1]);
            const mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];
            const ab = new ArrayBuffer(byteString.length);
            const ia = new Uint8Array(ab);

            for (let i = 0; i < byteString.length; i++) {
                ia[i] = byteString.charCodeAt(i);
            }

            return new Blob([ab], { type: mimeString });
        }

        // File upload function
        function uploadImage() {
            var formData = new FormData();
            var fileInput = document.getElementById('imageInput');
            formData.append('imageFile', fileInput.files[0]);

            // Send the image to the backend using AJAX
            var xhr = new XMLHttpRequest();
            xhr.open('POST', '/upload', true);
            xhr.onload = function () {
                if (xhr.status === 200) {
                    document.getElementById('result').innerHTML = 'Image uploaded successfully.';
                    processButton.disabled = false;
                } else {
                    document.getElementById('result').innerHTML = 'Error uploading image.';
                }
            };
            xhr.send(formData);
        }
    </script>
</body>
</html>
