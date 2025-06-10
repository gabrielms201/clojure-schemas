(ns alura-schemas.core-test
  (:require [alura-schemas.core :refer :all]
            [clojure.test :refer [deftest testing is]]))

(declare thrown? thrown-with-msg?)

(deftest a-testa
  (testing "aa"
    (is (thrown-with-msg? Exception #"does not match schema*" (extrai-idade {:idade "21"})))))
