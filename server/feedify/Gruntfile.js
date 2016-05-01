/*global module:false*/
module.exports = function(grunt) {

  grunt.loadNpmTasks('grunt-include-source');
  grunt.loadNpmTasks('grunt-wiredep');
  grunt.loadNpmTasks('grunt-remove');
  grunt.loadNpmTasks('grunt-execute');
  // These plugins provide necessary tasks.
  grunt.loadNpmTasks('grunt-contrib-concat');
  grunt.loadNpmTasks('grunt-contrib-uglify');
  grunt.loadNpmTasks('grunt-contrib-qunit');
  grunt.loadNpmTasks('grunt-contrib-jshint');
  grunt.loadNpmTasks('grunt-contrib-watch');
  grunt.loadNpmTasks('grunt-protractor-webdriver');
  grunt.loadNpmTasks('grunt-protractor-runner');




  // Project configuration.
  grunt.initConfig({
    // Metadata.
    pkg: grunt.file.readJSON('package.json'),
    banner: '/*! <%= pkg.title || pkg.name %> - v<%= pkg.version %> - ' +
      '<%= grunt.template.today("yyyy-mm-dd") %>\n' +
      '<%= pkg.homepage ? "* " + pkg.homepage + "\\n" : "" %>' +
      '* Copyright (c) <%= grunt.template.today("yyyy") %> <%= pkg.author.name %>;' +
      ' Licensed <%= _.pluck(pkg.licenses, "type").join(", ") %> */\n',
    // Task configuration.
    includeSource: {
      options: {
        basePath: 'app',
        baseUrl: '/',
      },
      app: {
        files: {
          'app/index.html': 'app/index.html.tpl'
        }
      }
    },
    execute: {
      server: {
        src: ['server.js']
      }
    },
    protractor_webdriver: {
      e2e: {
        options: {},
      },
    },
    protractor: {
      options: {
        configFile: "e2e-tests/protractor.conf.js", // Default config file
        keepAlive: false, // If false, the grunt process stops when the test fails.
        noColor: false, // If true, protractor will not use colors in its output.
        args: {
          verbose: true,
          baseUrl: "http://localhost:8000"
        }
      },
      all: {}
    },
    wiredep: {
      task: {
        // Point to the files that should be updated when
        // you run `grunt wiredep`
        src: [
          'app/index.html'
        ],
        fileTypes: {
          html: {
            replace: {
              js: '<script src="/{{filePath}}"></script>',
              css: '<link rel="stylesheet" href="/{{filePath}}" />'
            }
          }
        },
        options: {}
      }
    },
    remove: {
      options: {
        trace: true
      },
      fileList: ['app/index.html'],
      dirList: []
    },
    watch: {}
  });



  // Default task.
  grunt.registerTask('build', ['remove', 'includeSource', 'wiredep']);

  grunt.registerTask('run', ['execute']);

  grunt.registerTask('test', ['protractor_webdriver', 'protractor']);
};
