![jgl](http://i32.photobucket.com/albums/d9/_Silencer/JGL/jgl-512x212.png)

jgl
===

OpenGL OO abstractions.

This project is inspired on and aims to provide similar functionality as the [OGLPlus](http://oglplus.org "OGL Plus") C++ OpenGL framework.

With jgl, you get a set of wrapper classes which abstract most of the housekeeping tasks one inevitably faces when dealing with OpenGL. This framework is primarily focused on OpenGL 3 and later version since this the former is the currently accepted programming standard for OpenGL and is on par with DirectX feature wise.

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

Please note that jgl is a work in progress project. Therefore, not all of OGLPlus' features have been implemented yet. Last time I checked, Matúš Chochlík has around 60 or 70 OpenGL demos implemented using his framework. I have only ported around 25 so far. Please contact me if you think you might be able to help with the demo porting efforts.

## Examples

The source code includes some ports of the [OGPlus example set](http://oglplus.org/oglplus/html/examples.html). It is the best way to understand how to use jgl since they are very compact and concise.

If you'd like to see the demos that have been ported, please check the follwing Java Webstart examples:

TODO add webstart jnlp links from jgl-demos.

