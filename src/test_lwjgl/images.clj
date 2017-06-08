(ns test-lwjgl.images
  (:import [org.lwjgl BufferUtils]))
  

(defn io-to-byte-buffer 
  [path buffer-size]  
  
  (let [path (java.nio.file.Paths/get path (make-array String 0))
        file-channel (java.nio.file.Files/newByteChannel path (make-array java.nio.file.OpenOption 0))        buffer (BufferUtils/createByteBuffer (-> file-channel .size (+ 1)))] 

    [buffer file-channel] 
  )
)
