(ns alura-schemas.core
  (:require [schema.core :as s]))
(s/set-fn-validation! true?)

;; Paciente
(s/defschema TipoPessoa (s/enum :fisica :juridica))
(s/defschema Cobertura [(s/enum :raioX :ultrassom :tomografia)])

(s/defschema Plano {(s/required-key :cobertura) (s/maybe Cobertura)})

(s/defschema Paciente {(s/required-key :idade)       s/Num
                       (s/required-key :nome)        s/Str
                       (s/required-key :tipo-pessoa) TipoPessoa
                       (s/required-key :plano)       Plano
                       (s/optional-key :nascimento)  s/Str})

(s/defn extrai-idade :- s/Num
  [{:keys [idade] :as paciente} :- Paciente]
  idade)

(s/defn novo-paciente :- Paciente
  [idade :- (s/constrained s/Num (and pos? #(< % 100)))
   nome :- s/Str
   tipo-pessoa :- TipoPessoa
   plano :- Plano]
  {:idade idade :nome nome :tipo-pessoa tipo-pessoa :plano plano})

(def p1 (novo-paciente
         10
         "XPRRRTO"
         :fisica
         {:cobertura [:ultrassom :raioX]}))

(s/validate Paciente p1)
(extrai-idade p1)

;; Node e recursive
(s/defschema Node {(s/required-key :right) (s/maybe (s/recursive #'Node))
                   (s/required-key :left)  (s/maybe (s/recursive #'Node))
                   (s/required-key :value) s/Num})

(s/validate Node {:right nil :left nil :value 32})

(s/defn search :- (s/maybe Node)
  [node :- Node
   input :- s/Num]
  (when-let [{:keys [right left value]} node]
    (cond
      (= value input) node
      (> input value) (recur right input)
      :else (recur left input))))

(def n1
  {:right {:right nil :left nil :value 40}
   :left  {:right nil :left nil :value 20}
   :value 32})

(search n1 20)
(search n1 40)
(search n1 100)
(search n1 32)