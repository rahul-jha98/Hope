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
    .ref('/messages/{name}/{messageId}')
    .onCreate((snapshot, context) => {
        const messageData = snapshot.val()

        const data = checkout(messageData.message, messageData.senderName)

        if(messageData.senderName != "Mod") {
            if(data !== "") {
    
                const parentRef = snapshot.ref.parent
                if(parentRef !== null) {
                    parentRef.push({message: data, senderName:"Mod"})
                }
                
            }
        }
    })

function checkout(text: string, sender: string) : string {
    const text2 = text.toLowerCase().split(" ")
    if(text2.some(x => x === "hello") || text2.some(x => x ==="hi") || (text2.some(x => x ==="anyone") && text2.some(x => x ==="there"))) {
        return "Hi " + sender
    } else if (text2.some(x => x ==="want") && (text2.some(x => x ==="share") || text2.some(x => x ==="speak"))) {
        return "Everyone let's hear from " + sender + "."
    } else if(text2.some(x => x ==="that") && text2.some(x => x ==="was") && (text2.some(x => x ==="all") || text2.some(x => x ==="it"))){
        return "Thanks " + sender + ". It was nice hearing you."
    }
    return ""
}