INSERT
ignore
       INTO questions (id, text, type, lesson_id, correct_answer_id)
       VALUES (1, 'How to bath your cat?', 'MULTIPLE_CHOICE', 1, null),
       (2, 'How to feed your cat?', 'MULTIPLE_CHOICE', 1, null),
       (3, 'Does cats have a heart?', 'TRUE_FALSE', 1, 1),
       (4, 'How to bath your cat?', 'MULTIPLE_CHOICE', 2, null),
       (5, 'How to feed your cat?', 'MULTIPLE_CHOICE', 2, null),
       (6, 'Does cats have a heart?', 'TRUE_FALSE', 2, 1);

INSERT
ignore into options (id, text, question_id)
VALUES (3, 'Option 1', 1),
       (4, 'Option 2', 1),
       (5, 'Option 3', 1);

UPDATE questions
SET correct_answer_id =
        CASE
            WHEN id = 1 THEN 3
            WHEN id = 2 THEN 4
            ELSE null
            END
WHERE id IN (1, 2, 4, 5);