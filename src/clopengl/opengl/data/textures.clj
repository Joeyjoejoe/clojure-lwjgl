(ns clopengl.opengl.data.textures
  "https://www.khronos.org/opengl/wiki/Texture
  Textures parameters: http://docs.gl/es3/glTexParameter")

;; {
;;   :id nil
;;   :source ""
;;   :type nil         ;; GL_TEXTURE_1D, GL_TEXTURE_2D, GL_TEXTURE_3D, GL_TEXTURE_RECTANGLE etc
;;   :parameters {
;;     :wrap-s nil     ;; GL_TEXTURE_WRAP_S ~> GL_REPEAT, GL_MIRRORED_REPEAT, GL_CLAMP_TO_EDGE, GL_CLAMP_TO_BORDER
;;     :wrap-t nil     ;; GL_TEXTURE_WRAP_T ~> GL_REPEAT, GL_MIRRORED_REPEAT, GL_CLAMP_TO_EDGE, GL_CLAMP_TO_BORDER
;;     :wrap-r nil     ;; GL_TEXTURE_WRAP_R ~> GL_REPEAT, GL_MIRRORED_REPEAT, GL_CLAMP_TO_EDGE, GL_CLAMP_TO_BORDER
;;     :minifying nil  ;; GL_NEAREST, GL_LINEAR and more
;;     :magnifying nil ;; GL_NEAREST, GL_LINEAR and more
;;   }
;; }

(derive :type/texture1D :type/texture)
(derive :type/texture2D :type/texture)
(derive :type/texture3D :type/texture)

(defn texture
  "Creates a texture datastructure."
  [texture-type path]
  {:id nil
   :path path
   :type texture-type
   :parameters {}})

(defn +parameter
  "Set a texture parameter value"
  ([texture param value]
   (assoc-in texture [:parameters param] value)))
