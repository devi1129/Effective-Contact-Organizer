// users.js

// users.js

// users.js

 function confirmDeleteContact(contactId, currentPage) {
        Swal.fire({
            title: "Are you sure?",
            text: "You want to delete this contact..",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#d33",
            cancelButtonColor: "#3085d6",
            confirmButtonText: "Yes, delete it!",
        }).then((result) => {
            if (result.isConfirmed) {
                window.location = "/users/delete-contact/" + contactId + "/" + currentPage;
                
                 Swal.fire("Confirmed", "Your contact is deleted", "info");
            } else {
                Swal.fire("Cancelled", "Your contact is safe!", "info");
            }
        });
    }
    
    let page=0;
    
    let val;
    let query

    let tbody = document.querySelector("tbody");
    let initialContent = tbody.innerHTML;
    const search = async () => {
        query = document.getElementById("searchQueryInput");
         val = query.value.trim(); // Trim whitespace from the input value
   
        if (val =="") {
            // Handle empty input if needed
            tbody.innerHTML = initialContent;
            console.log(initialContent);
        } else {
          
            let url = `/api/search-contact/${val}/${page}`;

            const response = await fetch(url);
    
            if (response.ok) {
                const data = await response.json();
                updatetable(data.content); 
            } else {
                console.error("Failed to fetch data:", response.status, response.statusText);
            }
        }
    };
    
    const updatetable = (data) => {
       
      
        
    if (val === "") {
        // Handle empty input by restoring the initial content
        tbody.innerHTML = initialContent;
    } else 
        tbody.innerHTML = ""; // Clear the tbody

    

    
        data.forEach((contact) => {
            // Dynamically create a new row for each contact
            const newRow = document.createElement("tr");
    
            const nameCell = document.createElement("td");
            nameCell.innerHTML = `<img class="profile_pic" src="/contactprofiles/${contact.image}"  alt="Profile Photo" style="margin-right: 10px;"  />` +
                `<span>${contact.firstname}</span>`;
            newRow.appendChild(nameCell);
    
            const emailCell = document.createElement("td");
            emailCell.innerHTML = `<a href="/users/view-contact/${contact.cid}">${contact.email}</a>`;
            newRow.appendChild(emailCell);
    
            const phoneCell = document.createElement("td");
            phoneCell.innerText = contact.phone;
            newRow.appendChild(phoneCell);
    
            const actionsCell = document.createElement("td");
            actionsCell.innerHTML =
                `<a href="/users/edit-contact/${contact.cid}/${page}">` +
                `<i class="fa-solid fa-pen-to-square" style="color: black; margin-right: 7px;"></i>` +
                `</a>` +
                `<a href="/users/view-contact/${contact.cid}">` +
                `<i class="fa-regular fa-id-card" style="color: blue; margin-right: 7px;"></i>` +
                `</a>` +
                `<a href="#" onclick="confirmDeleteContact(${contact.cid},${page})" class="delete-icon">` +
                `<i class="fa-solid fa-trash" style="color: red;"></i>` +
                `</a>`;
            newRow.appendChild(actionsCell);
    
            tbody.appendChild(newRow);

           
        });

    };