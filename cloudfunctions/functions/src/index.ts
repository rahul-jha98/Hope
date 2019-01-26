import * as functions from 'firebase-functions';

// // Start writing Firebase Functions
// // https://firebase.google.com/docs/functions/typescript
//
// export const helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });


const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

export const moderatorBot = functions.database
    .ref('/messages/{messageId}')
    .onCreate((snapshot, context) => {
        const messageData = snapshot.val()

        const data = checkout(messageData.message, messageData.senderName)
        
        if(data !== "") {
    
            const parentRef = snapshot.ref.parent
            if(parentRef !== null) {
                parentRef.push({message: data, senderName:"Mod"})
            }
            
        }
        
    })

function checkout(text: string, sender: string) : string {
    const text2 = text.toLowerCase()
    if(text2.includes("hello")) {
        return "Hi " + sender
    } 
    return ""
}