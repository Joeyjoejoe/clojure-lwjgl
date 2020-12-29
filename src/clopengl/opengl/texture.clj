(ns clopengl.opengl.texture
  (:import (org.lwjgl.stb STBImage)
      	   (org.lwjgl BufferUtils)
	         (org.lwjgl.opengl GL11 GL30))
  (:require [clopengl.opengl.data.textures :as t]
            [clopengl.opengl.abstract.transformation :as transform]
            [clojure.java.io :as io]))

(def test-texture1
  (-> (t/texture "GL11/GL_TEXTURE_2D" "textures/container.jpg")
      (t/+parameter "GL_TEXTURE_WRAP_S" "GL_REPEAT")
      (t/+parameter "GL_TEXTURE_WRAP_T" "GL_REPEAT")
      (t/+parameter "GL_TEXTURE_MIN_FILTER" "GL_LINEAR")
      (t/+parameter "GL_TEXTURE_MAG_FILTER" "GL_LINEAR")))

(def test-texture2
  (-> (t/texture "GL11/GL_TEXTURE_2D" "textures/awesomeface.png")
      (t/+parameter "GL_TEXTURE_WRAP_S" "GL_REPEAT")
      (t/+parameter "GL_TEXTURE_WRAP_T" "GL_REPEAT")
      (t/+parameter "GL_TEXTURE_MIN_FILTER" "GL_LINEAR")
      (t/+parameter "GL_TEXTURE_MAG_FILTER" "GL_LINEAR")))

(defmethod transform/data->opengl! :type/texture
  [h & _]
  (let [id           (GL11/glGenTextures)
        texture-type (:type h)
        width        (BufferUtils/createIntBuffer 1)
        height       (BufferUtils/createIntBuffer 1)
        color        (BufferUtils/createIntBuffer 1)
        path         (.getAbsolutePath (io/file (io/resource (:path h))))
        parameters   (:parameters h)]

    (GL11/glBindTexture texture-type id)

    (doseq [[param v] parameters]
      ;; TODO Study and implement relevants edge cases: http://docs.gl/es3/glTexParameter
      ;;   - Depending on value type, determine and use glTexParmeter variants (glTexParameter{i,f,iv,fv})
      ;;   - Get constant (param) value from constant name (.get (.getDeclaredField org.lwjgl.opengl.GL11 "GL_TEXTURE_2D") nil)
      (GL11/glTexParameteri texture-type param v))

    ;; Flip the texture horizontaly, because OpenGL expects the 0.0 coordinate on the y-axis to be on the bottom side of the image, but images usually have 0.0 at the top of the y-axis
    (STBImage/stbi_set_flip_vertically_on_load true)

    ;; last parameter 3 represent the color channels: r,g,b.
    ;; It may vary depending on the texture image type (JPEG, PNG etc...)
    (if-let [texture-data (STBImage/stbi_load path width height color 3)]
      (do
	      (GL11/glTexImage2D texture-type 0 GL11/GL_RGB (.get width) (.get height) 0 GL11/GL_RGB GL11/GL_UNSIGNED_BYTE texture-data)
	      (GL30/glGenerateMipmap texture-type)
	      (STBImage/stbi_image_free texture-data))
      (throw (Exception. (str "Texture loading failed: " (STBImage/stbi_failure_reason) " at " path))))
    (-> h
      (assoc :id id))))

;; If need specialized implementation can be developed
;; (defmethod transform/data->opengl! :type/texture1D [h & _])
