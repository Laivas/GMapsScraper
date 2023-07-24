package scrapperModel;

import java.lang.reflect.Field;
import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "gmapsSerpModel")
public class GmapsSERPModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_Sequence")
	@SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
	public Long id;

	private String title;
	
	private String address;
	
//	private String description;
	
	private String phoneNumber;
	
	private String website;
	
	public String[] fieldsValuesToArray() {

		String[] fields = new String[this.getClass().getDeclaredFields().length];

		int index = 0;

		for (Field field : Arrays.asList(this.getClass().getDeclaredFields())) {

			try {

				if (!field.getName().matches("id")) {

					if (field.get(this) != null) {

						if (field.get(this).toString().contains(":")) {

							fields[index] = field.get(this).toString();

						} else

							fields[index] = field.get(this).toString();

					}

				}

			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			index++;

		}

		return Arrays.copyOfRange(fields, 1, fields.length);

	}
	
}
