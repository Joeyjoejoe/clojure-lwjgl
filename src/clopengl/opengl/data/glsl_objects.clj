(ns clopengl.opengl.data.glsl-objects
  "Data representation of OpenGL's [GLSL Objects](https://www.khronos.org/opengl/wiki/GLSL_Object).

   #### Programs:
   ```clojure
   {:program/id       1
    :program/uniforms []
    :program/shaders  []}
   ```
   A Program object represents fully processed executable code, in the OpenGL Shading Language, for one or more **shader stages**.

   #### Shaders:
   ```clojure
   {:shader/path \"path/to/file\"
    :shader/stage 1}
   ```
   Shader object represents compiled GLSL code for a single **shader stage**.

   |key|type|description|
   |-----|-----------|---------------------------------------------------------|
   |:shader/stage|Integer|One of the **shader stage** constants: `GL20/GL_VERTEX_SHADER`, `GL20/GL_FRAGMENT_SHADER` etc...|
   |:shader/path|String|Path to the shader source file.|

   #### Uniforms:
   ```clojure
   {:uniform/name \"varname\"
    :uniform/location 1
    :uniform/value nil
    :uniform/type}
   ```
   A uniform is a global Shader variable. These act as parameters that the user of a shader program can pass to that program.

   |key|type|description|
   |-----|-----------|---------------------------------------------------------|
   |:uniform/name|String|It corresponds to a variable name in a shader|
   |:uniform/location|Integer|When a shader is compiled, each uniform receive a location number used to retrieve it and set its value|
   |:uniform/value|Any|The value that will be pass to the shader's variable|
   |:uniform/type|String|The GLSL data type of the value (see: https://www.khronos.org/opengl/wiki/Data_Type_(GLSL))|
  "
  (:require [clojure.spec.alpha :as s]
            [clopengl.opengl.util.basic-specs :as _]))

(s/def :glsl/program (s/keys :req [:program/shaders :program/uniforms]
                             :opt [:program/id]))
(defonce blank-program {
  :program/id nil
  :program/uniforms {}
  :program/shaders []
})


(s/def :glsl/shader (s/keys :req [:shader/path :shader/stage]))
(defonce blank-shader {
  :shader/path nil
  :shader/stage nil
})


(s/def :glsl/uniform (s/keys :req [:uniform/name :uniform/type]
                             :opt [:uniform/location :uniform/value]))
(defonce blank-uniform {
  :uniform/name nil
  :uniform/location nil
  :uniform/value nil
  :uniform/type nil
})


(s/def :program/id (s/nilable :clopengl/id))
(s/def :program/uniforms map?)
(s/def :program/shaders vector?)
(s/def :shader/path :clopengl/path)
(s/def :shader/stage int?)
(s/def :uniform/name string?)
(s/def :uniform/location (s/nilable int?))
(s/def :uniform/value (s/nilable any?))
(s/def :uniform/type string?)
