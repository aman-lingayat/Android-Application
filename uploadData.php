<?php
		$connect = mysqli_connect("localhost","root","","projectdata");
		if(isset($_POST['image']))
		{		
		$target_dir = "Images/";
		$description = $_POST['description'];
		$image = $_POST['image'];
		$imageStore = rand()."_".time().".jpeg";
		$target_dir = $target_dir."/".$imageStore;  
		file_put_contents(base64_decode($image));
		$select = "INSERT INTO 'workdata'('id','description','image') VALUES (NULL,'$description','$filename')";
		$responce = mysqli_query($connect,$select);	
		if($responce)
				{
					echo "Image Uploaded";
					mysqli_close($connect);
				}
		else{
				echo "Failed to Upload";
		}
		}


//$conn = mysqli_connect("localhost","root","");
//mysqli_select_db($conn,"projectdata");

//			$description = $_POST['description'];
//			$image = $_POST['image'];
			
//			$filename = "IMG".rand()."jpeg";
//			file_put_contents("images/".$filename,base64_decode($img));
			
//							$qry = "INSERT INTO 'workdata'('id','description','image')
//									VALUES(NULL,'$description','$filename')";
							
//							$responce = mysqli_query($conn,$qry);
							
//							if($responce==true)
//							{
//								echo "Image Uploaded";
//							}
//							else{
//								echo "Failed to Upload";
//							}
		?>