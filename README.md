# clopengl
A toy project to render a 3D environment in clojure using lwjgl. 

## Usage
* clone the repo: git clone git@github.com:Joeyjoejoe/clopengl.git
* Install dependancies: lein deps
* run it: lein run

## Loading models
In order to load a 3d model, you first need to use an appropriate parser for the file format you're using.

### Parser
A parser must return a data structure compatible with the vertex-setup function. At the moment it should be a map with a mandatory key **vertices** and an optional key **indices**.
* **vertices** must contain a vector of vertex which are represented as this: `{:coordinates [x y z], :color [r g b]}`
* **indices** can be set to avoid vertex duplication. It's a vector of vertex index from the **vertices** vector: `[0 1 2 2 3 0 4 5 6]`

At the moment, there is only one parser available for .ply files (extracted from blender, i did not test with other software).
The parser is located **engine/utilities/parser_3d**.

#### Example:
Here is an exemple of the data structure returned by a parser for simple red plane rectangle.

###### with indices:
```clojure
{:vertices [{:coordinates [-0.5 0.0 -0.5], :color [255 0 0]}
            {:coordinates [ 0.5 0.0 -0.5], :color [255 0 0]}
            {:coordinates [ 0.5 0.0  0.5], :color [255 0 0]}
            {:coordinates [-0.5 0.0  0.5], :color [255 0 0]}]
 :indices [0 1 2 2 3 0]}
```
When rendered, indices are grouped by 3 to form a mesh (a triangle formed with 3 vertices). Here indice 0 match the first element in :vertices collection, 1 match the second etc... The rectangle will be formed of 2 meshes (0 1 2) and (2 3 0).

###### without indices
To load the same rectangle without indices, we should have feed all the meshes (group of 3 vertices) in the **vertices** key:
```clojure
{:vertices [{:coordinates [-0.5 0.0 -0.5], :color [255 0 0]}
            {:coordinates [ 0.5 0.0 -0.5], :color [255 0 0]}
            {:coordinates [ 0.5 0.0  0.5], :color [255 0 0]}
            {:coordinates [ 0.5 0.0  0.5], :color [255 0 0]}
            {:coordinates [-0.5 0.0  0.5], :color [255 0 0]}
            {:coordinates [-0.5 0.0 -0.5], :color [255 0 0]}]}
```
The same vertices are set multiple time in this collection. Using indices permit to avoid theses duplication and is very usefull with big models.

### Vertex setup
When you have parsed your model file, all you need to do is call the `clopengl.engine.opengl.vertices/setup` function with the data structure returned by the parser. Optionaly, you can pass an integer as second argument to set the number of instances of the model you want to render.

`setup` will return a function containing opengl routine to render model. You must pass this function to the **to-render-functions** argument of the main loop of core.clj file.

```clojure
(clopengl.engine.opengl.vertices/setup parsed-model 10)
;; will render 10 times the model
````
At the moment, the models are rendered at random location
