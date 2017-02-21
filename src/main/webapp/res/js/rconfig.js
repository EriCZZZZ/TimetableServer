/**
 * require config
 * Created by eric on 17-2-12.
 */
require.config({
    baseUrl: "/res/js/3rd",
    paths: {
        "jquery" : "jquery/jquery-3.1.1.min",
        "backbone" : "backbone/backbone-min",
        "underscore" : "underscore/underscore-min",
        "bootstrap" : "bootstrap-3.3.7-dist/js/bootstrap.min"
    },
    map: {
        '*': {
            'css': 'require/css'
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
            deps:['css!bootstrap-3.3.7-dist/css/bootstrap.min', 'jquery']
        }
    }
});