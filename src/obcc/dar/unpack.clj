;; Licensed to the Apache Software Foundation (ASF) under one
;; or more contributor license agreements.  See the NOTICE file
;; distributed with this work for additional information
;; regarding copyright ownership.  The ASF licenses this file
;; to you under the Apache License, Version 2.0 (the
;; "License"); you may not use this file except in compliance
;; with the License.  You may obtain a copy of the License at
;;
;;   http://www.apache.org/licenses/LICENSE-2.0
;;
;; Unless required by applicable law or agreed to in writing,
;; software distributed under the License is distributed on an
;; "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
;; KIND, either express or implied.  See the License for the
;; specific language governing permissions and limitations
;; under the License.

(ns obcc.dar.unpack
  (:require [clojure.java.io :as io]
            [obcc.dar.read :as dar.read]))

(defn unpack [index outputdir verbose]
  (when (.exists outputdir)
    (throw (Exception. (str "output directory " (.getAbsolutePath outputdir) " exists"))))

  (dorun
   (for [[path item] index]
     (let [entry (:entry item)
           outputfile (io/file outputdir path)]
       (io/make-parents outputfile)
       (with-open [is (dar.read/entry-stream item)
                   os (io/output-stream outputfile)]
         (when verbose
           (println (:sha1 entry) (:path entry) (str "(" (:size entry) " bytes)")))
         (io/copy is os))))))
