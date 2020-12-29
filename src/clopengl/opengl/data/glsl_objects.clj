(ns clopengl.opengl.data.glsl-objects
  "Data representation of OpenGL's [GLSL Objects](https://www.khronos.org/opengl/wiki/GLSL_Object).

   #### Programs:
   ```clojure
   {:id       1
    :uniforms [Uniforms]
    :shaders  [Shaders]}
   ```
   A Program Object represents fully processed executable code, in the OpenGL Shading Language, for one or more **shader stages**.

   #### Shaders:
   ```clojure
   {:path \"path/to/shader\"
    :type 1}
   ```
   Shader objects represent compiled GLSL code for a single **shader stage**.

   |key|type|description|
   |-----|-----------|---------------------------------------------------------|
   |:type|Integer|One of the **shader stage** constants: `GL20/GL_VERTEX_SHADER` or `GL20/GL_FRAGMENT_SHADER` etc...|
   |:path|String|Path to the shader source file.|

   #### Uniforms:
   ```clojure
   {:name \"varname\"
    :location 1
    :value nil}
   ```
  ")

(defn program
  "Creates a program datastructure."
  []
  {:id nil
   :type :type/program
   :uniforms {}
   :shaders  []})

(defn +shader
  "Creates a shader datastructure and place it in the provided program (if any)."
  ([path stage]
   {:path path
    :type :type/shader
    :stage stage})
  ([program path stage]
   (let [shader (+shader path stage)]
     (update program :shaders conj shader))))

(defn +uniform
  "Creates a uniform datastructure with or without value and store it in the provided programe (if any)."
  ([uname vtype]
   {:name uname
    :type :type/uniform
    :value nil
    :vtype vtype
    :location nil})
  ([program uname vtype]
   (let [uniform (+uniform uname vtype)]
     (assoc-in program [:uniforms uname] uniform))))

(defn uniform=
  "Set program's uniform `uname` value"
  [program uname value]
  (assoc-in program [:uniforms uname :value] value))
