<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>User Login</title>
</head>
<body>

<div id="app">
  <h2>User Login</h2>
  <form @submit.prevent="submitForm">
    <label for="phone">Phone Number (10 digits only):</label>
    <input v-model="phone" type="tel" id="phone" name="phone" placeholder="Enter your 10-digit phone number" pattern="[0-9]{10}" required>
    <br>
    <label for="password">Password:</label>
    <input v-model="password" type="password" id="password" name="password" placeholder="Enter your password" required>
    <br>
    <button type="submit">Login</button>
  </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/vue@2"></script>
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>

<script>
new Vue({
  el: '#app',
  data() {
    return {
      phone: '',
      password: ''
    };
  },
  methods: {
    submitForm() {
      const formData = {
        phone: this.phone,
        password: this.password
      };

      // Replace 'YOUR_BACKEND_URL' with the actual URL of your backend endpoint
      axios.post('YOUR_BACKEND_URL', formData)
        .then(response => {
          // Handle the response from the backend
          console.log('Response from server:', response.data);
        })
        .catch(error => {
          // Handle errors
          console.error('Error:', error);
        });
    }
  }
});
</script>

</body>
</html>
