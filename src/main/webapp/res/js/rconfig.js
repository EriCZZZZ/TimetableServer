/**
 * require config
 * Created by eric on 17-2-12.
 */
require.config({
    baseUrl: "/res/js",
    paths: {
        "jquery" : "3rd/jquery/jquery-3.1.1.min",
        "backbone" : "3rd/backbone/backbone-min",
        "underscore" : "3rd/underscore/underscore-min",
        "bootstrap" : "3rd/bootstrap-3.3.7-dist/js/bootstrap.min",
        "eposition" : "eframework/position"
    },
    map: {
        '*': {
            'css': '3rd/require/css'
        }
    },
    shim: {
        'backbone' : {
            deps: ['underscore', 'jquery'],
            exports: 'Backbone'
        },
        'underscore': {
            exports: '_'
        },
        'bootstrap' : {
            deps:['css!3rd/bootstrap-3.3.7-dist/css/bootstrap.min', 'jquery']
        },
        "eposition" : {
            deps:['jquery']
        }
    }
});