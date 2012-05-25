class CreateUsers < ActiveRecord::Migration
  def change
    create_table :users do |t|
      t.string :email
      t.string :name
      t.integer :id_avatar
      t.integer :piggies
      t.integer :keys
      t.integer :num_done
      t.integer :num_success

      t.timestamps
    end
  end
end
