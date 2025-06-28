This project is developed for Software Engineering Course in Marmara University-Engineering Faculty-Computer Engineering Department.
Developers are Eren Ahmet Yeşiltaş and İlker İslam Yetimoğlu.

Topic: 
An AI-powered story-creation/contribution platform.
In this platform, there are 2 main roles which are **Author** and **Contributor**.

**Author** can do these:
1. Start a story with its main text.

**Contributor** can do these:
1. They can contribute stories with add a new branch to the main story or already existed branches.
2. They do not have to contribute to stories. If they want, they just read the stories in this platform.

**Both** can do these:
1. Making a review for stories and branches. Also, they can give a point from 1 to 5.  
2. Giving a reaction as like, dislike to the stories and branches.

**AI** is used for these:
1. Creating a new branch as AI read the main text and existed branch. In this way, it can create a suitable new branch to associated story.
In this way, contributers can get assistment from the power of AI.

Technical things:
1. **Security**: JWT is used for authentication and authorization. Spring Security is also used.
2. **AI**: OpenAI-API is used.
3. **Database**: Mysql is used. Also database migration is used when they are needed.
4. **Architecture**: N-Layer Architecture is preferred.

Currently, frontend is developing phase...
