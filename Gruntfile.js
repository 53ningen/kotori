module.exports = function(grunt) {
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),

        connect: {
            site: {
                options: {
                    hostname: 'localhost',
                    port: 9000,
                    base: 'src/main/resources/templates/'
                }
            }
        },

        watch: {
            static_files: {
                files: ['**/*.html', '**/*.css'],
                options: {
                    livereload: true
                }
            },
            js_files: {
                files: '**/*.js'
            }
        }
    });

    grunt.loadNpmTasks('grunt-contrib-connect');
    grunt.loadNpmTasks('grunt-contrib-watch');

    grunt.registerTask('default', ['connect', 'watch']);
};