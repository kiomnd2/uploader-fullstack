const path = require('path');

module.exports = {
  outputDir: path.resolve(__dirname, '../backend/src/main/resources/static'),
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:9000',
      },
    },
  },
  configureWebpack: {
    resolve: {
      alias: {
        '@': path.join(__dirname, 'src'),
      },
    },
  },
};
