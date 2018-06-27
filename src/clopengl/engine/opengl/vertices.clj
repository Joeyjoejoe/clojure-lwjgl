(ns clopengl.engine.opengl.vertices
  (:use [clopengl.engine.utilities.misc])
  (:require [clopengl.engine.opengl.shader-program :as program]
            [clopengl.engine.opengl.buffers :as buffer]
	          [clopengl.engine.opengl.uniforms :as uniform]
            [clopengl.engine.utilities.transformations :as transformation]
            [clopengl.engine.state.global :as state])
  (:import (org.lwjgl.opengl GL11 GL13 GL20 GL30 GL31)))

(defn setup [shape instances]
  "Take a list of vertices (coordinates [x y z] of a pixel and optionaly its color), indices is a list of index from vertices, used to describe triangles."
  (let [vertices (:vertices shape)
        indices (:indices shape)
        vao-id (GL30/glGenVertexArrays)
        _ (GL30/glBindVertexArray vao-id)
	      ;;texture1-id (textures/setup "src/clopengl/assets/textures/container.jpg")
	      ;;texture2-id (textures/setup "src/clopengl/assets/textures/awesomeface.png")
        program-id (program/init)
	      instances-coords (rand-positions instances)
	      points-count (if (= 0 (count indices)) (count vertices) (count indices) )]

    (buffer/create-vbo vertices)
    (buffer/create-pbo instances-coords)
    (if (< 0 (count indices))
        (buffer/create-ebo indices))
    ;; You can unbind the VAO afterwards so other VAO calls won't accidentally modify this VAO, but this rarely happens. Modifying other VAOs requires a call to glBindVertexArray anyways so we generally don't unbind VAOs (nor VBOs) when it's not directly necessary.
    (GL30/glBindVertexArray 0)

    ;;(GL11/glPolygonMode GL11/GL_FRONT_AND_BACK GL11/GL_LINE)

    ;; Bind Texture to uniform in shader
    (program/bind program-id)
    ;;(GL20/glUniform1i (uniform/get-location program-id, "texture1") 0)
    ;;(GL20/glUniform1i (uniform/get-location program-id, "texture2") 1)

    ;; projection matrix (perspective)
    (GL20/glUniformMatrix4fv (uniform/get-location program-id "projection") false (buffer/create-float-buffer (transformation/make "perspective-projection" [45.0 (/ 1280.0 960.0) 0.1 100.0])))
    (def model-position (uniform/get-location program-id "model"))
    (def view-position (uniform/get-location program-id "view"))

    (program/unbind)

    ;; Return a function with draw code
    (fn [camera]
      (program/bind program-id)
      (GL20/glUniformMatrix4fv view-position false camera)
      ;; Texture
      ;;(GL13/glActiveTexture GL13/GL_TEXTURE0)
      ;;(GL11/glBindTexture GL11/GL_TEXTURE_2D texture1-id)
      ;;(GL13/glActiveTexture GL13/GL_TEXTURE1)
      ;;(GL11/glBindTexture GL11/GL_TEXTURE_2D texture2-id)

      (GL30/glBindVertexArray vao-id)

      (if (> instances 1)
        ;; Draw multiple instances
        (if (= 0 (count indices))
    	    (GL31/glDrawArraysInstanced GL11/GL_TRIANGLES 0 points-count instances) ;; Normal draw
    	    (GL31/glDrawElementsInstanced GL11/GL_TRIANGLES points-count GL11/GL_UNSIGNED_INT 0 instances)) ;; Draw with indices
        ;; Draw single shape
        (if (= 0 (count indices))
    	    (GL11/glDrawArrays GL11/GL_TRIANGLES 0 points-count) ;; Normal draw
          (GL11/glDrawElements GL11/GL_TRIANGLES points-count GL11/GL_UNSIGNED_INT 0)))))) ;; Draw with indices
