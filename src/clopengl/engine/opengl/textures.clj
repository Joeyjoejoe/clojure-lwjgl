(ns clopengl.engine.opengl.textures
  (:import (org.lwjgl.stb STBImage)
	   (org.lwjgl BufferUtils)
	   (org.lwjgl.opengl GL11 GL30)))

(defn setup [path]
  (let [tbo-id (GL11/glGenTextures)
	width (BufferUtils/createIntBuffer 1)
	height (BufferUtils/createIntBuffer 1)
	color (BufferUtils/createIntBuffer 1)
	desired-color-channels 3]

    (GL11/glBindTexture GL11/GL_TEXTURE_2D tbo-id)

    ;; set the texture wrapping/filtering options (on the currently bound texture object)
    (GL11/glTexParameteri GL11/GL_TEXTURE_2D GL11/GL_TEXTURE_WRAP_S GL11/GL_REPEAT)
    (GL11/glTexParameteri GL11/GL_TEXTURE_2D GL11/GL_TEXTURE_WRAP_T GL11/GL_REPEAT)
    (GL11/glTexParameteri GL11/GL_TEXTURE_2D GL11/GL_TEXTURE_MIN_FILTER GL11/GL_LINEAR)
    (GL11/glTexParameteri GL11/GL_TEXTURE_2D GL11/GL_TEXTURE_MAG_FILTER GL11/GL_LINEAR)

    ;; Flip the texture horizontaly, because OpenGL expects the 0.0 coordinate on the y-axis to be on the bottom side of the image, but images usually have 0.0 at the top of the y-axis
    (STBImage/stbi_set_flip_vertically_on_load true)

    (if-let [texture-data (STBImage/stbi_load path width height color desired-color-channels)]
      (do
	      (GL11/glTexImage2D GL11/GL_TEXTURE_2D 0 GL11/GL_RGB (.get width) (.get height) 0 GL11/GL_RGB GL11/GL_UNSIGNED_BYTE texture-data)
	      (GL30/glGenerateMipmap GL11/GL_TEXTURE_2D)
	      (STBImage/stbi_image_free texture-data))
      (throw (Exception. (str "Texture loading failed: " (STBImage/stbi_failure_reason) " at " path))))

    tbo-id))
