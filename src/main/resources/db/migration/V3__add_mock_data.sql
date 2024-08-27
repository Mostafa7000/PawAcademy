INSERT ignore INTO units (id, description, image, name, exam)
VALUES (1, 'Here we will learn the basic techniques of dealing with pets and domestic animals',
        'media/units/flamingo.jpeg', 'Basics',
        'https://docs.google.com/forms/d/e/1FAIpQLSdE7Ykht1rDqyoCXOZjAZqMj06H4ihJ1mu6zi1MLzC0wfs2LA/viewform?usp=sharing'),
       (2, 'Now we will dive deeper into the kingdom of animals ', 'media/units/husky.jpeg', 'Intermediate',
        'https://docs.google.com/forms/d/e/1FAIpQLSdE7Ykht1rDqyoCXOZjAZqMj06H4ihJ1mu6zi1MLzC0wfs2LA/viewform?usp=sharing'),
       (3,
        'Advanced topics about how to attend to not just the physical aspects of your pets, but also their psychological side too',
        'media/units/parrot.jpeg', 'Advanced',
        'https://docs.google.com/forms/d/e/1FAIpQLSdE7Ykht1rDqyoCXOZjAZqMj06H4ihJ1mu6zi1MLzC0wfs2LA/viewform?usp=sharing');

INSERT ignore into lessons(id, name, content, video, unit_id)
VALUES (1, 'Introduction', 'Pets are more than just animals; they are family members, confidants, and sources of comfort. Each type of pet comes with its own unique set of characteristics, care requirements, and ways to interact. Whether you''re a seasoned pet owner or considering welcoming a new furry, feathered, or scaled friend into your home, understanding the basics of pet care and the responsibilities that come with it is essential.',
        'https://www.youtube.com/watch?v=i-80SGWfEjM', 1),
       (2, 'Understanding Different Species', 'We will delve into the various aspects of pet ownership, including: \n
            1.Choosing the Right Pet: Understanding your lifestyle, living conditions, and personal preferences to select a pet that fits seamlessly into your life.\n
            2.Basic Care: From nutrition and exercise to grooming and healthcare, we''ll cover the essentials of keeping your pet healthy and happy. \n
            3. Training and Socialization: Learning how to train your pet and socialize them with other animals and people, which is crucial for their well-being and your peace of mind. \n
            4. The Bond Between Humans and Pets: Exploring the emotional and psychological benefits of pet ownership, including how pets can reduce stress, improve mental health, and provide a sense of purpose.\n
            5. Responsible Ownership: Discussing the importance of spaying/neutering, microchipping, and understanding the commitment of a pet''s lifespan. \n',
        'https://www.youtube.com/watch?v=i-80SGWfEjM', 1);
