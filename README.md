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
