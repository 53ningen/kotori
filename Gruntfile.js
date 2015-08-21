module.exports = function(grunt) {
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),

        connect: {
            site: {
                options: {
                    hostname: 'localhost',
                    port: 9001,
                    base: 'src/main/resources/templates/'
                }
            }
        },

        watch: {
            html_files: {
                files: '**/*.html',
                options: {
                    livereload: true
                }
            },
            scss_files: {
                files: '**/*.scss',
                tasks: 'compass',
                options: {
                    livereload: true
                }
            },
            js_files: {
                files: '**/*.js'
            }
        },

        compass: {
            compile: {
                options: {
                    config: 'config.rb'
                }
            }
        }
    });

    grunt.loadNpmTasks('grunt-contrib-connect');
    grunt.loadNpmTasks('grunt-contrib-compass');
    grunt.loadNpmTasks('grunt-contrib-watch');

    grunt.registerTask('default', ['connect', 'watch', 'compass']);
};