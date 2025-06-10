(ns alura-schemas.core
  (:require [schema.core :as s]))
(s/set-fn-validation! true?)


;; Paciente
(s/defschema TipoPessoa (s/enum :fisica :juridica))

(s/defschema Paciente {(s/required-key :idade) s/Int
                       (s/required-key :nome) s/Str
                       (s/required-key :tipo-pessoa) TipoPessoa})

(s/defn extrai-idade :- s/Int
  [paciente :- Paciente]
  (:idade paciente))

(s/defn novo-paciente :- Paciente
  [idade :- (s/constrained s/Int (and pos? #(< % 100)))
   nome :- s/Str
   tipo-pessoa :- TipoPessoa]
  {:idade idade :nome nome :tipo-pessoa tipo-pessoa})

(def p1 (novo-paciente 10 "XPRRRTO" :fisica))
(s/validate Paciente p1)
(extrai-idade p1)

;; Node e recursive
(s/defschema Node {(s/required-key :right) (s/maybe (s/recursive #'Node))
                   (s/required-key :left) (s/maybe (s/recursive #'Node))
                   (s/required-key :value) s/Int})

(s/validate Node {:right nil :left nil :value 32})
