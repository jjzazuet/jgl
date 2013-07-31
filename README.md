jgl
===

A Java wrapper for OpenGL© version 3 and higher.

This project is inspired on and aims to provide similar functionality as the [OGLPlus](http://oglplus.org "OGL Plus") C++ OpenGL framework. jgl makes use of the [JOGL](http://jogamp.org/jogl/www/) Java library to access OpenGL in a cross platform fashion.

With jgl, you get a set of wrapper classes which abstract most of the housekeeping tasks one inevitably faces when dealing with OpenGL. This framework is primarily focused on OpenGL 3 since it is the currently accepted programming standard for OpenGL and is on par with DirectX feature wise (if not more ;)).

## Installation

Currently, this library may only be built from source. During its first major release, artifacts will be available on Maven Central.

## Compiling from source

First, clone the jgl Github repository:

    git clone https://github.com/jjzazuet/jgl.git

Once you have the source code, use [Gradle 1.4+](http://gradle.org) for building all the required projects.

    gradle clean assemble

In case you need the libraries as Maven artifacts, you may install them to your local M2 repository:

    gradle clean install

## Current Caveats

Please note that jgl is a work in progress project. Therefore, not all of OGLPlus' features have been implemented yet. Last time I checked, Matúš Chochlík has around 110 OpenGL demos implemented using his framework. I have only ported around 22 so far. Please contact me if you think you might be able to help with the demo porting effort.

## Examples

The source code includes some ports of the [OGPlus example set](http://oglplus.org/oglplus/html/examples.html). It is the best way to understand how to use jgl since they are very compact and concise.

If you'd like to see the demos that have been ported, please check the follwing Java Webstart examples:
