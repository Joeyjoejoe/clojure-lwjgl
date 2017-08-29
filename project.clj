(defproject test-lwjgl "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [net.mikera/vectorz-clj "0.47.0"]
		 [thi.ng/math "0.2.1"]
                 [org.slf4j/slf4j-log4j12 "1.7.12"]
                 [org.lwjgl/lwjgl "3.0.0b"]
                 [org.lwjgl/lwjgl-platform "3.0.0b" :classifier "natives-linux"]
                 [org.lwjgl/lwjgl-platform "3.0.0b" :classifier "natives-windows"]
                 [org.lwjgl/lwjgl-platform "3.0.0b" :classifier "natives-osx"]]
  :main test-lwjgl.core) 
